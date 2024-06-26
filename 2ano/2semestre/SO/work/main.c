#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

#define MAX_SIZE 8
#define ROWS 8

// Global variables
int num_processos = 0;
int current_time = 0;

// Process states
#define NEW 0
#define READY 1
#define RUNNING 2
#define BLOCKED 3
#define EXIT 4

// Structs
typedef struct {
    int buffer[MAX_SIZE];
    int front;
    int rear;
    int size;
} queue;

typedef struct {
    int cpuState;
    int cpuPid;
} cpuState;

typedef struct {
    int processId;
    int state;
    int blockedTime;
    int runningTime;
    int arrivalTime;
    int runningTimeIndex;
    int blockedTimeIndex;
} process;

// Function to initialize the queue
void initializeQueue(queue *queue) {
    queue->front = 0;
    queue->rear = -1;
    queue->size = 0;
}

// Function to check if the queue is empty
int isEmpty(queue *queue) {
    return queue->size == 0;
}

// Function to check if the queue is full
int isFull(queue *queue) {
    return queue->size == MAX_SIZE;
}

// Function to enqueue an element
void enqueue(queue *queue, int value) {
    if (isFull(queue)) {
        printf("Queue is full. Unable to enqueue element.\n");
        return;
    }

    queue->rear = (queue->rear + 1) % MAX_SIZE;
    queue->buffer[queue->rear] = value;
    queue->size++;
}

// Function to dequeue an element
int dequeue(queue *queue) {
    if (isEmpty(queue)) {
        printf("Queue is empty. Unable to dequeue element.\n");
        return -1;
    }

    int value = queue->buffer[queue->front];
    queue->front = (queue->front + 1) % MAX_SIZE;
    queue->size--;

    return value;
}

// Function to peek at the front element of the queue without dequeueing
int peek(queue *queue) {
    if (isEmpty(queue)) {
        printf("Queue is empty. Unable to peek.\n");
        return -1;
    }
    return queue->buffer[queue->front];
}

// Function to display the elements in the queue
void displayQueue(queue *queue) {
    if (isEmpty(queue)) {
        printf("Queue is empty.\n");
        return;
    }

    printf("Queue: ");
    int i = queue->front;
    int count = 0;
    while (count < queue->size) {
        printf("%d ", queue->buffer[i]);
        i = (i + 1) % MAX_SIZE;
        count++;
    }
    printf("\n");
}

// Function to order the readyQueue based on the remaining running time
void orderReadyQueue(process *processes, queue *readyQueue) {
    for (int i = 0; i < readyQueue->size - 1; i++) {
        for (int j = 0; j < readyQueue->size - i - 1; j++) {
            int processIndex1 = (readyQueue->front + j) % MAX_SIZE;
            int processIndex2 = (readyQueue->front + j + 1) % MAX_SIZE;
            int remainingTime1 = processes[readyQueue->buffer[processIndex1]].runningTime;
            int remainingTime2 = processes[readyQueue->buffer[processIndex2]].runningTime;
            if (remainingTime1 > remainingTime2) {
                int temp = readyQueue->buffer[processIndex1];
                readyQueue->buffer[processIndex1] = readyQueue->buffer[processIndex2];
                readyQueue->buffer[processIndex2] = temp;
            }
        }
    }
}

// Function to initialize cpu
void init_cpu(cpuState *cpu) {
    cpu->cpuPid = -1;
    cpu->cpuState = -1;
}

void printState (process *processes)
{
    int aux = 0 ; 

    printf("  %d       \t", current_time );
    while (aux<num_processos) 
    {
        switch (processes[aux].state)
        {
        case 0 :
            printf("  NEW    \t" );
            break;
        
         case 1 :
            printf("  READY    \t" );
            break;
        
         case 2 :
            printf("  RUNNING    \t" );
            break;
         
         case 3 :
            printf("  BLOCKED    \t" );
            break;
        
         case 4 :
            printf("  EXIT    \t" );
            break;
        
        default:
            break;
        }
        
        
        aux++; 
    }

    printf("\n\n");

}

// Create a process
process createProcess(int **program) {
    process processo;
    processo.processId = num_processos;
    processo.state = NEW;
    processo.blockedTime = program[num_processos][2];
    processo.runningTime = program[num_processos][1];
    processo.arrivalTime = current_time;
    processo.runningTimeIndex = 1;
    processo.blockedTimeIndex = 2;
    return processo;
}

// Function to handle new timeout
void newTimeout(cpuState *cpu, queue *readyQueue, process *processes, int lines, int **program) {

    int i = 0;

    while(i< num_processos){
        
        if(processes[i].state == NEW && processes[i].arrivalTime == current_time - 1 && isEmpty(readyQueue) && cpu->cpuState == -1){
            cpu->cpuPid = processes[i].processId;
            cpu->cpuState = RUNNING;
            processes[i].state = RUNNING;
            return;
        }
        if(processes[i].state == NEW && processes[i].arrivalTime == current_time - 1 && cpu->cpuState == RUNNING){
            enqueue(readyQueue, processes[i].processId);
            processes[i].state = READY;
            orderReadyQueue(processes, readyQueue);  // Order the readyQueue based on remaining running time
            return;
        }
        i++;
    }
}

// Function to handle new state

void newState(process *processes, int lines, int **program){
    int i = 0 ;
    while(i < lines ){
        if(program[i][0] == current_time){
        processes[num_processos] = createProcess(program); 
        num_processos++;
        }
    i++;
    }
}

// Function to handle running to blocked
void readyToRunning(queue *readyQueue, cpuState *cpu, process *processes){
    if(cpu->cpuState == -1 && isEmpty(readyQueue) == 0){
        cpu->cpuPid = dequeue(readyQueue);
        cpu->cpuState = RUNNING;
        processes[cpu->cpuPid].state = RUNNING;
        processes[cpu->cpuPid].runningTime--;
    }
}

// Function to handle running timeout
void runningTimeout(cpuState *cpu, queue *blockedQueue, process *processes, int **program){
    if(cpu->cpuState == RUNNING){
        //processes[cpu->cpuPid].runningTime--;
        if(processes[cpu->cpuPid].runningTime == 0){
            if(processes[cpu->cpuPid].blockedTime == 0){
                processes[cpu->cpuPid].state = EXIT;
                cpu->cpuPid = -1;
                cpu->cpuState = -1;
            }
            else{
                processes[cpu->cpuPid].state = BLOCKED;
                processes[cpu->cpuPid].runningTimeIndex += 2;  // Increment the index by 2 for the next running time
                int currentIndex = processes[cpu->cpuPid].runningTimeIndex;  // Current index of the running time
                if(currentIndex >= 8){
                    return;
                }
                processes[cpu->cpuPid].runningTime = program[cpu->cpuPid][currentIndex];
                enqueue(blockedQueue, cpu->cpuPid);
                cpu->cpuPid = -1;
                cpu->cpuState = -1;
            }
        }
        processes[cpu->cpuPid].runningTime--;
    }
}

// Function to handle blocked to ready
void blockedToReady(process *processes, queue *blockedQueue, queue *readyQueue, int **program) {
    int size = blockedQueue->size;
    int pidList[size];
    int count = 0;

    // Dequeue all PIDs from blockedQueue and add them to pidList
    while (!isEmpty(blockedQueue)) {
        int pid = dequeue(blockedQueue);
        pidList[count] = pid;
        count++;
    }

    // Process each PID in pidList
    for (int i = 0; i < count; i++) {
        int pid = pidList[i];
        if (processes[pid].blockedTime == 0) {
            processes[pid].blockedTimeIndex += 2;
            int currentIndex = processes[pid].blockedTimeIndex;
            processes[pid].blockedTime = program[pid][currentIndex];
            processes[pid].state = READY;
            enqueue(readyQueue, pid);
        } else {
            enqueue(blockedQueue, pid);
            processes[pid].blockedTime--;
        }
    }
    orderReadyQueue(processes, readyQueue);
}



int main() {
    int lines = 0;
    int rows = 0;
    int **program;
    int aux = 0;

    cpuState cpu;
    queue readyQueue;
    queue blockedQueue;
    process processes[ROWS];

    init_cpu(&cpu);
    initializeQueue(&readyQueue);
    initializeQueue(&blockedQueue);

    int programas[3][8] = {
    {0, 5, 1, 3, 3, 4, 0, 0 } ,
    {1, 7, 2, 4, 1, 2, 0, 0 } ,
    {2, 1, 1, 5, 1, 1, 0, 0 } };

    lines = sizeof(programas) / sizeof(programas[0]);
    rows = sizeof(programas[0]) / sizeof(programas[0][0]);

    program = malloc(lines * sizeof *program);
    for (int i = 0; i < lines; i++) {
        program[i] = malloc(rows * sizeof *program[i]);
    }

    for (int i = 0; i < lines; i++) {
        for (int j = 0; j < rows; j++) {
            program[i][j] = programas[i][j];
        }
    }

    
    printf("\n");
    printf("CURRENT TIME \t | PROC1 \t | PROC2 \t | PROC3 \t | PROC4 \t | PROC5 \t | PROC6 \t | PROC7 \t | PROC8 \t | \n");
    printf("\n");
    
    
     while (aux < 40) {
        newState(processes, lines, program);
        newTimeout(&cpu, &readyQueue, processes, lines, program);
        runningTimeout(&cpu, &blockedQueue, processes, program);
        readyToRunning(&readyQueue, &cpu, processes);
        blockedToReady(processes, &blockedQueue, &readyQueue, program);
        printState(processes); 
        printf("\n");
        printf("\n");
        current_time++;
        aux++;
    }   

    // Free allocated memory
    for (int i = 0; i < lines; i++) {
        free(program[i]);
    }
    free(program);
  
    
    
}