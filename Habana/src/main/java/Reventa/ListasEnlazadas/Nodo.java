package Reventa.ListasEnlazadas;

public class Nodo<E> {
    private Nodo next;
    private Nodo back;
    private E info;

    public Nodo(E info) {
        this.next = null;
        this.back = null;
        this.info = info;
    }

    public Nodo getNext() {
        return next;
    }

    public void setNext(Nodo next) {
        this.next = next;
    }

    public void setBack(Nodo back) {this.back = back;}

    public Nodo getBack() {
        return back;
    }

    public E getInfo() {
        return info;
    }

    public void setInfo(E info) {
        this.info = info;
    }
}
