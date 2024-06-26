#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "func.h"

struct node {
    char *username;
    struct node *back;
    struct node *next;
};

void insert_new_client(struct node *head, char *user) {
    struct node *temp = malloc(sizeof(struct node));
    temp->username = user;
    temp->next = NULL;
    temp->back = NULL;
    if(head == NULL)
        head = temp;
    else {
        struct node *aux = head;
        while(aux->next != NULL)
            aux = aux->next;
        temp->back = aux;
        aux->next = temp;
    }
}

void remove_client(struct node *temp, char *user) {
}

bool exists_client(struct node *head, char *user) { //verifica se já existe algum cliente com o mesmo nome que o novo cliente
    struct node *temp = head;
    while(temp->next != NULL) {
        if(temp->username == user)
            return true;
        temp = temp->next;
    }
    if(temp->next == NULL && temp->username == user)
        return true;
    return false;
}

/*void main() {
    struct node *head = NULL;
    struct node *end = NULL;
}*/