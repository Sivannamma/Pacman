/*
 * third party -> connecting between pacmanBoard and pacmanController
 * once the pacmanController updates its coordinates, then we set the coordinates on the pacmanBoard in order to repaint the pacman
 */

public interface MovementConnecter {
	void setCoordinates(int x, int y);
}
