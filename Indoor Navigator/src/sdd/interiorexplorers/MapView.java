package sdd.interiorexplorers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sdd.interiorexplorers.handlers.MapHandler;
import sdd.interiorexplorers.map.Floor;
import sdd.interiorexplorers.map.Floor.Area;
import sdd.interiorexplorers.map.Floor.Edge;
import sdd.interiorexplorers.map.Floor.Elevator;
import sdd.interiorexplorers.map.Floor.Node;
import sdd.interiorexplorers.map.Types;
import sdd.thirdparty.fullscroll.FullScrollView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class MapView extends View {

	// private members

	private final int ELEV_RADIUS = 8;
	private final float SCALE_STEP = 1.1f;
	private float scaleFactor_;
	private Floor floor_;
	private int maxX_ = 0;
	private int maxY_ = 0;

	private List<ShapeDrawable> drawables;
	private List<int[]> objectPairs_;
	private List<float[]> lines_;
	private Map<Integer, Rect> idToRect_;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - current context for the map
	 * @param attrs
	 *            - set of view attributes
	 */
	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		drawables = new ArrayList<ShapeDrawable>();
		idToRect_ = new TreeMap<Integer, Rect>();
		lines_ = new ArrayList<float[]>();
		objectPairs_ = new ArrayList<int[]>();
		scaleFactor_ = 1;
	}

	/**
	 * Centers the view on a node
	 * 
	 * @param id
	 *            - node id
	 */
	public void showById(int id) {
		try {
			Rect shapeToCenter = idToRect_.get(id);
			this.requestRectangleOnScreen(shapeToCenter, true);
		} catch (Exception e) {
			if (currentFloor + 1 < MapHandler.getInstance().getCurrentMap()
					.getFloor().size()) {
				currentFloor = currentFloor + 1;
				this.setFloor(MapHandler.getInstance().getCurrentMap()
						.getFloor().get(currentFloor));
				this.printPath(lastnode1, lastnode2);
				this.showById(id);
			}
		}
	}

	/**
	 * Defines key stroke events
	 * 
	 * @param keyCode
	 *            - code for the pressed key
	 * @return - if key press performed an operation or not
	 */
	int currentFloor = 0;

	public boolean keyPressed(int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			setScaleFactor(scaleFactor_ * SCALE_STEP);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			setScaleFactor(scaleFactor_ / SCALE_STEP);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			currentFloor = currentFloor - 1;
			if (currentFloor < 0) {
				currentFloor = 0;
			} else {
				drawables.clear();
				idToRect_.clear();
				lines_.clear();
				objectPairs_.clear();
				scaleFactor_ = 1;
				this.setFloor(MapHandler.getInstance().getCurrentMap()
						.getFloor().get(currentFloor));
				this.printPath(lastnode1, lastnode2);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			currentFloor = currentFloor + 1;
			if (currentFloor >= MapHandler.getInstance().getCurrentMap()
					.getFloor().size()) {
				currentFloor = MapHandler.getInstance().getCurrentMap()
						.getFloor().size() - 1;
			} else {
				drawables.clear();
				idToRect_.clear();
				lines_.clear();
				objectPairs_.clear();
				scaleFactor_ = 1;
				this.setFloor(MapHandler.getInstance().getCurrentMap()
						.getFloor().get(currentFloor));
				this.printPath(lastnode1, lastnode2);
			}
			return true;
		}
		return false;
	}

	/**
	 * Sets the view's scale factor (for zooming)
	 * 
	 * @param scaleFactor
	 *            - zoom scale
	 */
	public void setScaleFactor(float scaleFactor) {
		scaleFactor_ = scaleFactor;
		setFloor(floor_);
	}

	/**
	 * Adds areas to the view
	 * 
	 * @param areas
	 *            - list of areas to be included in the view
	 */
	private void addAreas(List<Area> areas) {
		for (Area area : areas) {
			if (area.getBottom() * scaleFactor_ > maxY_) {
				maxY_ = (int) (area.getBottom() * scaleFactor_);
			}
			if (area.getRight() * scaleFactor_ > maxX_) {
				maxX_ = (int) (area.getRight() * scaleFactor_);
			}
			ShapeDrawable areaToAdd = new ShapeDrawable(new RectShape());
			Rect areaRect = new Rect((int) (area.getLeft() * scaleFactor_),
					(int) (area.getTop() * scaleFactor_),
					(int) (area.getRight() * scaleFactor_),
					(int) (area.getBottom() * scaleFactor_));
			areaToAdd.setBounds(areaRect);
			idToRect_.put(area.getId(), areaRect);
			areaToAdd.getPaint().setARGB(255, 255, 255, 255);
			drawables.add(areaToAdd);
		}
	}

	/**
	 * Adds elevators to the view
	 * 
	 * @param elevators
	 *            - list of elevators to be added
	 */
	private void addElevators(List<Elevator> elevators) {
		for (Elevator elev : elevators) {
			addCircle(0, 255, 0, elev.getX(), elev.getY(), elev.getId(), true);
		}
	}

	/**
	 * Adds nodes to the view
	 * 
	 * @param nodes
	 *            - list of nodes to be added
	 */
	private void addNodes(List<Node> nodes) {
		for (Node node : nodes) {
			addCircle(255, 0, 0, node.getX(), node.getY(), node.getId(), !node
					.getType().equals(Types.HIDDEN));
		}
	}

	/**
	 * Draws a line on the view
	 * 
	 * @param from
	 *            - first node in the line
	 * @param to
	 *            - second node in the line
	 */
	private void drawLine(int from, int to) {
		Rect first = idToRect_.get(from);
		Rect second = idToRect_.get(to);
		float[] line = new float[4];
		line[0] = first.exactCenterX();
		line[1] = first.exactCenterY();
		line[2] = second.exactCenterX();
		line[3] = second.exactCenterY();
		lines_.add(line);
	}

	/**
	 * Adds a line to the view
	 * 
	 * @param from
	 *            - first node in the line
	 * @param to
	 *            - second node in the line
	 */
	private void connectPoints(int from, int to) {
		List<Edge> edges = floor_.getEdge();
		for (Edge e : edges) {
			if ((e.getFromId() == from && e.getToId() == to)
					|| (e.getFromId() == to && e.getToId() == from)) {
				try {
					drawLine(from, to);
					int[] pairToAdd = new int[2];
					pairToAdd[0] = from;
					pairToAdd[1] = to;
					objectPairs_.add(pairToAdd);
				} catch (Exception ee) {
					//
				}
			}
		}
	}

	/**
	 * This function prints the shortest path from node1 to node2
	 * 
	 * @param node1
	 *            - start node
	 * @param node2
	 *            - end node
	 * @return - if successful path was found
	 */
	int lastnode1 = 0;
	int lastnode2 = 0;

	public boolean printPath(int node1, int node2) {
		lastnode1 = node1;
		lastnode2 = node2;
		System.out.println("Path from " + String.valueOf(node1) + " to "
				+ String.valueOf(node2));
		List<String> path = MapHandler.getInstance().getCurrentGraph()
				.Dijkstra(String.valueOf(node1), String.valueOf(node2));

		// clear current path
		this.clearConnections();
		if (path.size() <= 1) {
			return false;
		}

		// print the path
		for (int i = 1; i < path.size(); ++i) {
			this.connectPoints(Integer.parseInt(path.get(i - 1)),
					Integer.parseInt(path.get(i)));
		}

		return true;
	}

	/**
	 * Clears all lines and object pairs from the view
	 */
	private void clearConnections() {
		lines_.clear();
		objectPairs_.clear();
	}

	/**
	 * Adds a circle to the view
	 * 
	 * @param r
	 *            - red color value
	 * @param g
	 *            - green color value
	 * @param b
	 *            - blue color value
	 * @param x
	 *            - x coordinate
	 * @param y
	 *            - y coordinate
	 * @param id
	 *            - node id
	 * @param draw
	 *            - Whether to draw a circle
	 */
	private void addCircle(int r, int g, int b, int x, int y, int id,
			Boolean draw) {
		// determine coordinates
		int xCoord = (int) ((x + ELEV_RADIUS) * scaleFactor_);
		int yCoord = (int) ((y + ELEV_RADIUS) * scaleFactor_);
		if (yCoord > maxY_) {
			maxY_ = yCoord;
		}
		if (xCoord > maxX_) {
			maxX_ = xCoord;
		}
		// draw the shape
		ShapeDrawable elevToAdd = new ShapeDrawable(new OvalShape());
		Rect areaRect = new Rect((int) ((x - ELEV_RADIUS) * scaleFactor_),
				(int) ((y - ELEV_RADIUS) * scaleFactor_), xCoord, yCoord);
		// add the shape
		elevToAdd.setBounds(areaRect);
		idToRect_.put(id, areaRect);
		elevToAdd.getPaint().setARGB(255, r, g, b);
		if (draw)
			drawables.add(elevToAdd);
	}

	/**
	 * sets and draws a new floor
	 * 
	 * @param floor
	 *            - floor to be viewed
	 */
	public void setFloor(Floor floor) {
		// remove all current view items
		floor_ = floor;
		drawables.clear();
		lines_.clear();
		idToRect_.clear();
		maxX_ = 0;
		maxY_ = 0;
		// add new view items
		// for(Floor f:MapHandler.getInstance().getCurrentMap().getFloor()){
		addAreas(floor.getArea());
		addElevators(floor.getElevator());
		addNodes(floor.getNode());
		// }
		this.currentFloor = MapHandler.getInstance().getCurrentMap().getFloor()
				.indexOf(floor);
		this.setMinimumHeight(maxY_ + 50);
		this.setMinimumWidth(maxX_ + 50);
		for (int[] connection : objectPairs_) {
			drawLine(connection[0], connection[1]);
		}
		FullScrollView parent = (FullScrollView) this.getParent();
		parent.removeAllViews();
		parent.addView(this);
		this.invalidate();
		parent.setMapView(this);
	}

	/**
	 * Draws all items in the view
	 */
	protected void onDraw(Canvas canvas) {
		for (ShapeDrawable s : drawables) {
			s.draw(canvas);
		}
		Paint linePaint = new Paint();
		linePaint.setARGB(255, 0, 255, 0);
		for (float[] line : lines_) {
			canvas.drawLines(line, linePaint);
		}
		try {
			for (Node n : floor_.getNode()) {
				if (n.getType().equals(Types.HIDDEN))
					continue;
				int x = (int) ((n.getX() + ELEV_RADIUS) * scaleFactor_);
				int y = (int) ((n.getY() + ELEV_RADIUS) * scaleFactor_);
				Paint textColor = new Paint();
				textColor.setARGB(255, 0, 128, 128);
				canvas.drawText(n.getDescription(), x, y, textColor);
			}
		} catch (Exception e) {
			Log.i("test", "e : " + e.toString());
		}
	}

}
