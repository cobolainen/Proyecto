package Interfaz;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

import blockChain.Monedero;

/**
 * Esta clase es la encargada de gestionar el JList de la pantalla principal es
 * la encargada de mostrar los objetos en la lista eliminarlos de la lista y de
 * añadirlos
 *
 */
public class ModeloLista extends AbstractListModel {
	private ArrayList<Object> lista = new ArrayList<>();

	@Override
	public int getSize() {
		return lista.size();
	}

	@Override
	public Object getElementAt(int index) {
		if (lista.get(index) instanceof Monedero) {
			Monedero p = (Monedero) lista.get(index);
			return p.name;
		}else {
			return lista.get(index);
		}
	}

	/**
	 * Metodo que añade un objeto contacto a la lista que se pasa por parametro
	 * 
	 * @param c
	 *            Objeto del tipo contacto que se va a itrducir en la lista
	 */
	public void anadirElemento(Object c) {
		lista.add(c);
		this.fireIntervalAdded(this, getSize(), getSize() + 1);
	}
	public void anadirElemento(int ind, Object c) {
		lista.add(ind, c);
		this.fireIntervalAdded(this, getSize(), getSize() + 1);
	}

	/**
	 * Elimina un objeto de la lista pasando por parametro su posicion en la misma
	 * 
	 * @param indice
	 */
	public void eliminarMonedero(int indice) {
		lista.remove(indice);
		this.fireIntervalRemoved(indice, getSize(), getSize() + 1);
	}

	/**
	 * Devuelve un el objeto contacto que se encuentra en la posicion de la lista
	 * que se pasa por parametro
	 * 
	 * @param indice
	 *            Posicion de la lista que ocupa el objeto que se va a tomar
	 * @return Devuelve un objeto del tipo contacto
	 */
	public Object getElement(int indice) {
		if (lista.get(indice) instanceof Monedero) {
		return (Monedero) lista.get(indice);
		}else {
			return lista.get(indice);
		}
	}
	public int size() {
		return lista.size();
	}

}
