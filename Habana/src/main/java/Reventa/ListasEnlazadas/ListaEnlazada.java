package Reventa.ListasEnlazadas;

public class ListaEnlazada<E> {
    protected Nodo first;
    protected Nodo end;
    protected int size;

    public ListaEnlazada() {
        this.first = null;
        this.end = null;
        this.size = 0;
    }

    public void add(E info) {
        Nodo<E> aux = new Nodo<>(info);

        if (first == null) {
            first = aux;
            end = aux;
            first.setBack(end);
        } else {
            Nodo<E> puntero = first;

            while (puntero.getNext() != first && puntero.getNext() != null) {
                puntero = puntero.getNext();
            }

            puntero.setNext(aux);
            end = aux;
            end.setNext(first);
            end.setBack(puntero);
            first.setBack(end);
        }
        size++;
    }

    public void add(int index, E info) {
        if (index >=0 && index < size) {

            Nodo aux = first;
            Nodo<E> nuevo = new Nodo<>(info);

            if (index == 0) {
                nuevo.setNext(first);
                first = nuevo;
                size++;
                return;
            }

            for (int j = 1; j <= size; j++) {
                if (index == j) {
                    nuevo.setNext(aux.getNext());
                    aux.setNext(nuevo);
                    size++;
                    return;
                }
                aux = aux.getNext();
            }
        } else {
            throw new IndexOutOfBoundsException("" +
                    "Indice fuera de rango");
        }
    }

    public E get(int index) {
        if (index >= 0 && index < size) {
            Nodo<E> aux = first;

            for (int i = 0; i < index; i++) {
                aux = aux.getNext();
            }

            return aux.getInfo();
        } else {
            throw new IndexOutOfBoundsException(
                    "indice fuera de rango."
            );
        }
    }

    public void remove(int index) {
        if (index == 0) {
            first = first.getNext();
            size--;
            return;
        }

        if (index >= 0 && index < size) {

            Nodo<E> anterior = first;
            Nodo<E> actual = first.getNext();

            for (int i = 1; i < size; i++) {
                if (index == i) {
                    anterior.setNext(actual.getNext());
                }
                anterior = actual;
                actual = actual.getNext();
            }
            size--;
        } else {
            throw new IndexOutOfBoundsException(
                    "indice fuera de rango."
            );
        }
    }

    public void replace(int index, E info) {
        if (index >= 0 && index < size) {
            Nodo aux = first;

            for (int i = 0; i < size; i++) {
                if (index == i) {
                    aux.setInfo(info);
                }

                aux = aux.getNext();
            }
        } else {
            throw new IndexOutOfBoundsException(
                    "Indice fuera de rango."
            );
        }
    }

    public void desplazar() {
        first = end;
        Nodo<E> aux = first;

        for (int i = 0; i < size - 1; i++) {
            aux = aux.getNext();
        }

        end = aux;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}