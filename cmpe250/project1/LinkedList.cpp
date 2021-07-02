#include "LinkedList.h"

LinkedList::LinkedList() {
    this->length=0;
    this->head = NULL;
    this->tail = NULL;
}

LinkedList::LinkedList(const LinkedList& list) {
    this->length=0;
    Node* copyNode = list.head;
    while(copyNode!=NULL) {
        pushTail(copyNode->name, copyNode->amount);
        copyNode=copyNode->next;
    }
}

LinkedList& LinkedList::operator=(const LinkedList &list) {
    this->length=0;
    if(this->head)
    delete head;
    Node* copyNode = list.head;
    while(copyNode!=NULL) {
        pushTail(copyNode->name, copyNode->amount);
        copyNode=copyNode->next;
    }
    return *this;
}

LinkedList::LinkedList(LinkedList&& list) {
    this->length=move(list.length);
    list.length=0;
    this->head = move(list.head);
    this->tail = move(list.tail);
    list.head = NULL;
    list.tail=NULL;

}

LinkedList& LinkedList::operator=(LinkedList &&list) {
    this->length=move(list.length);
    list.length=0;
    if(this->head)
    delete head;
    this->head = move(list.head);
    this->tail=move(list.tail);
    list.head=NULL;
    list.tail=NULL;
    return *this;
}

LinkedList::~LinkedList() {
    this->length=0;
    if(head)
    delete head;
}

void LinkedList::pushTail(string _name, float _amount) {
    if(head==NULL) {
        head = new Node(_name, _amount);
        tail=head;
    } else {
        Node *current = head;
        Node *temp= NULL;
        while(current!=NULL) {
            temp=current;
            current = current->next;
        }
        current= new Node(_name, _amount);
        temp->next=current;
        tail=current;
    }
    length++;

}

void LinkedList::updateNode(string _name, float _amount) {
    string currentName = head->name;
    Node *current=head;
    while(currentName!=_name) {
        current = current->next;
        currentName = current->name;
    }
    current->amount=_amount;
}


