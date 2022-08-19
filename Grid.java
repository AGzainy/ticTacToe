package tictactoe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Grid implements IGameObject{
	
	private ArrayList<Placement> placements = new ArrayList<Placement>(Main.SIZE);
	private Marker[][] markers;
	
	private int gridThickness = 16;
	private int markerIndex = 0;
	private boolean gameEnd = false;
	private int winType = -1;
	
	public Grid() {
		
		markers = new Marker[Main.ROWS][Main.ROWS];
		
		
		for  (int i  =0; i < Main.SIZE ; i++) {
			int xIndex = i % Main.ROWS;
			int yIndex = i / Main.ROWS;
			int size = Main.WIDTH / Main.ROWS;
			//System.out.println(xIndex);
			placements.add(new Placement(xIndex * size, yIndex * size, xIndex, yIndex, size, size));
			
		}
		reset();
		
	}

	@Override
	public void update(float deltaTime) {
		for (Placement placement : placements) {
			placement.update(deltaTime);
		}
		for (int x = 0; x < markers.length; x++) {
			for (int y = 0; y < markers.length; y++) {
				if(markers[x][y]== null) {
					continue;
				}
			  markers[x][y].update(deltaTime);
			}
		}
		
	}

	@Override
	public void render(Graphics2D graphicsRender) {
		for (Placement placement : placements) {
			placement.render(graphicsRender);
		}
		
		for (int x = 0; x < markers.length; x++) {
			for (int y = 0; y < markers.length; y++) {
				if(markers[x][y]== null) {
					continue;
				}
			  markers[x][y].render(graphicsRender);
			}
		}
		
		
		
		renderGrid(graphicsRender);
		
	}

	private void renderGrid(Graphics2D graphicsRender) {
		graphicsRender.setColor(new  Color(0x003001));
		
		int rowSize = Main.WIDTH / Main.ROWS;
		for (int x = 0; x < Main.ROWS +1; x++) {
			graphicsRender.fillRect(x * rowSize - (gridThickness/ 2), 0, gridThickness, Main.WIDTH);
			for (int y = 0; y < Main.ROWS +1; y++) {
				graphicsRender.fillRect(0, y * rowSize - (gridThickness/ 2), Main.HEIGHT, gridThickness);
				
			}
			
		}
		
		graphicsRender.setColor(Color.white);
		
		if(gameEnd) {
			
			drawEndGameOverlay(graphicsRender);
		}
		
	}

	private void drawEndGameOverlay(Graphics2D graphicsRender) {
		graphicsRender.setColor(new Color(0,0,0,(int)(225 * 0.5f)));
		
		graphicsRender.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		graphicsRender.setColor(Color.white);
		
		if(winType == -1) {
			// tie
			String g = "It's a tie!";
			 Font af = new Font("SansSerif", Font.PLAIN, 30);
			  graphicsRender.setFont (af);
			graphicsRender.drawString(g, 195, 235);
		}else {
			// won
			
			 Font af = new Font("Courier New", Font.PLAIN, 30);
			  graphicsRender.setFont (af);
			graphicsRender.drawString((winType == 0 ? "X" : "O") + " has won !" , 175, 235);
		}
		  String a = "press anywhwere to restart ;)";
		  Font af = new Font("SansSerif", Font.PLAIN, 25);
		  graphicsRender.setFont (af);
		
		graphicsRender.drawString(a, 85, 270);
	}

	public void mouseMoved(MouseEvent e) {
		if(gameEnd) {
			return;
		}
		
		for (Placement placement : placements) {
			placement.checkCollision(e.getX(), e.getY() -30);
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		for ( Placement placement : placements) {
			if(placement.isActive()) {
				placement.set(true);
				
				int x = placement.getxIndex();
				int y = placement.getyIndex();
				placeMarker(x, y);
				
			}
		}
		
	}
	public void placeMarker(int moveIndex) {
		placeMarker(moveIndex % Main.ROWS, moveIndex / Main.ROWS);
		
	}
	private void placeMarker(int x, int y) {
		markers[x][y] = new Marker(x, y, markerIndex);
		
		markerIndex ++;
		
		ArrayList<Marker> winLine = Checker.checkWin(markers);
		
		if(winLine != null) {
			winLine.forEach(marker -> marker.setWon(true));
			winType = winLine.get(0).getType();
			gameEnd = true;
		}else if(markerIndex >= Main.SIZE) {
			gameEnd = true;
		}
	}
	public void reset() {
		
		for (int x = 0; x < markers.length; x++) {
			for (int y = 0; y < markers.length; y++) {
				markers[x][y]= null;
			}
		}
		
		for (Placement placement : placements) {
			placement.set(false);
		}
	
		gameEnd = false;
		winType= -1;
		markerIndex = 0;
	}
	
	public boolean isGameEnd() {
		return gameEnd;
	}

	public int getTurn() {
		
		return markerIndex % 2;
	}

	public Marker[][] getMarkers() {
		
		return markers;
	}

	
	

}
