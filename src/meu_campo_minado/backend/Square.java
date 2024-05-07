package meu_campo_minado.backend;

import java.util.ArrayList;
import java.util.List;

public class Square {

	private String text;

	private boolean open;
	private boolean mine;
	private boolean flag;

	private List<SquareObserver> observers = new ArrayList<>();

	public void registerObserver(SquareObserver observer) {
		observers.add(observer);
	}

	public void notifyObsevers(FieldEvents event) {

		for (SquareObserver observer : observers) {

			if (event == FieldEvents.OPEN) {
				observer.update(this, FieldEvents.OPEN);
			} else if (event == FieldEvents.MARK) {
				observer.update(this, FieldEvents.MARK);
			} else if (event == FieldEvents.UNMARK) {
				observer.update(this, FieldEvents.UNMARK);
			} else if (event == FieldEvents.EXPLOSION) {
				observer.update(this, FieldEvents.EXPLOSION);
			}
		}
	}

	public Square() {
		setText("?");
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setMine() {
		this.mine = true;
	}

	public boolean getMine() {
		return mine;
	}

	public void setOpen() {
		this.open = true;
	}

	public boolean getOpen() {
		return open;
	}

	public void setFlag() {
		if (!getFlag()) {
			this.flag = true;
		} else {
			this.flag = false;
		}
	}

	public boolean getFlag() {
		return flag;
	}

	public String toString() {
		return getText();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Square other = (Square) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}
