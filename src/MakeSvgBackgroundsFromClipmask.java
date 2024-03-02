package reflectionplotter;

import java.awt.Point;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

public class MakeSvgBackgroundsFromClipmask {

static Vector shapeGeometry = new Vector();	
static Vector shapeWidth = new Vector();	
static Vector shapeHeight = new Vector();	

static Vector allPolygons = new Vector();

static boolean allhastobeinside = false;

//80 rows = 14 pix

public static int possibilities = 0; //16 is 40x40 and 48 rows
public static int shapestartindex = 0; //16 is 40x40 and 48 rows

public static String rootDir = "C:\\Users\\pinda\\Documents\\_________angelsanddemons\\______reflexion\\theart\\_________55\\";
public static String clipFile = "11_finaltext.svg";





public static Polygon drawingArea = new Polygon();

public static enum Background {  BYTEGAN20, BYTEGAN40, BYTEGAN60, BYTEGANTEXT, HEX20, HEX40, HEX60, HEX80, BINARY20, BINARY37, BINARY50, BINARY75, BINARY100, BINARY150};

public MakeSvgBackgroundsFromClipmask() {
	drawingArea = new Polygon();
	drawingArea.addPoint(50,50);
	drawingArea.addPoint(50,1150);
	drawingArea.addPoint(1150,1150);
	drawingArea.addPoint(1150,50);
	drawingArea.addPoint(50,50);
	initializeGeometry();
}

public static String plotSVG(String svg, String type, String dims) {

	String plottedSVG = "";
	boolean fromPolygons = false;
	boolean fromPaths = false;
	boolean fromText = false;
	
	String thissvg = svg;
	if (thissvg.indexOf("<path")!=-1) {
		fromPaths = true;
	} 
	if ( thissvg.indexOf("<polygon")!=-1) {
		fromPolygons = true;
	}
	if (thissvg.indexOf("<text")!=-1) {
		fromText = true;
	}
	
	if (type.equals("overlay")) {
		if (dims.equals("binary")) {
			double b20 = 6;
			possibilities = 2;
			shapestartindex = 0;
			plottedSVG = getLineArtFromText(svg,Background.BINARY20, b20, false);
		}
		if (dims.equals("bytegans")) {
			double by20 = 10;
			possibilities = 3;
			shapestartindex = 16;
			String rawpoly = svg;
			plottedSVG = getLineArtInsideRawPolygon(rawpoly, Background.BYTEGAN20, by20, true);
		}
	}	
	
	
	if (type.equals("bytegans")) {
		if (dims.equals("40x40")) {
			double by40 = 5;
			possibilities = 3;
			shapestartindex = 16;
			String rawpoly = svg.substring(svg.indexOf("d="));
			rawpoly = rawpoly.substring(rawpoly.indexOf("M")+1, rawpoly.indexOf("z",rawpoly.indexOf("M")) );
			rawpoly = "<polygon points=\""+rawpoly+"\"/>";
			plottedSVG = getLineArtInsideRawPolygon(rawpoly, Background.BYTEGAN40, by40, false);
		}
	}	
	
	if (type.equals("background")) {
		if (dims.equals("owner") || dims.equals("hex")) {
			double h60 = 3;
			possibilities = 16;
			shapestartindex = 0;
			String noWhiteSpace = svg;
			noWhiteSpace = noWhiteSpace.replace("\r", "");
			noWhiteSpace = noWhiteSpace.replace("\n", "");
			String rawpoly = noWhiteSpace.substring(noWhiteSpace.indexOf("d=\"M")+4, noWhiteSpace.indexOf("z",noWhiteSpace.indexOf("d=\"M")) );
			rawpoly = "<polygon points=\""+rawpoly+"\"/>";
			plottedSVG = getLineArtOutsideRawPolygon(rawpoly, Background.HEX60, h60);
		}
		if (dims.equals("byte")) {
			double by20 = 10;
			possibilities = 3;
			shapestartindex = 16;
			String rawpoly = svg.substring(svg.indexOf("d="));
			rawpoly = rawpoly.substring(rawpoly.indexOf("M")+1, rawpoly.indexOf("z",rawpoly.indexOf("M")) );
			//rawpoly =  "0 0 "+rawpoly+" 0 0 0 1200 1200 1200 1200 0";
			rawpoly = "<polygon points=\""+rawpoly+"\"/>";
			plottedSVG = getLineArtOutsideRawPolygon(rawpoly, Background.BYTEGAN20, by20);
		}

	//0 0 300 360 300 840 420 840 420 720 540 720 540 960 660 960 660 720 780 720 780 840 900 840 900 360 300 360 0 0 0 1200 1200 1200 1200 0 
	}	
	
	if (type.equals("skull")) {
		if (dims.equals("bin80")) {
			double b75 = 2;
			possibilities = 2;
			shapestartindex = 0;

			if (fromPolygons) {
				String rawpoly = svg.substring(svg.indexOf("d=\"M")+4, svg.indexOf("z",svg.indexOf("d=\"M")) );
				rawpoly = "<polygon points=\""+rawpoly+"\"/>";
				plottedSVG = getLineArtInsideRawPolygon(rawpoly, Background.BINARY75, b75,  false);
			}
			if (fromPaths) {
				plottedSVG = getLineArtFromMinifiedPaths(svg, Background.BINARY75, b75);
			}
		}
		if (dims.equals("bin48")) {
			double b50 = 3;
			possibilities = 2;
			shapestartindex = 0;

			if (fromPolygons) {
				String rawpoly = svg.substring(svg.indexOf("d=\"M")+4, svg.indexOf("z",svg.indexOf("d=\"M")) );
				rawpoly = "<polygon points=\""+rawpoly+"\"/>";
				plottedSVG = getLineArtInsideRawPolygon(rawpoly, Background.BINARY50, b50, false);
			}
			if (fromPaths) {
				plottedSVG = getLineArtFromMinifiedPaths(svg, Background.BINARY50, b50);
			}
		}
		if (dims.equals("hex48")) {
			double b50 = 3;
			possibilities = 16;
			shapestartindex = 0;

			if (fromPolygons) {
				String rawpoly = svg.substring(svg.indexOf("d=\"M")+4, svg.indexOf("z",svg.indexOf("d=\"M")) );
				rawpoly = "<polygon points=\""+rawpoly+"\"/>";
				plottedSVG = getLineArtInsideRawPolygon(rawpoly, Background.HEX60, b50,  false);
			}
			if (fromPaths) {
				plottedSVG = getLineArtFromMinifiedPaths(svg, Background.HEX60, b50);
			}
		}
		
	}	
	
	
	return plottedSVG ;
}

public static void main(String[] args) {

	String clipFileString = readInFileToString( rootDir+clipFile );
	
	boolean fromPolygons = false;
	boolean fromPaths = false;
	boolean fromText = false;
	
	
	if (clipFileString.indexOf("<path")!=-1) {
		fromPaths = true;
	}
	if (clipFileString.indexOf("<polygon")!=-1) {
		fromPolygons = true;
	}
	if (clipFileString.indexOf("<text")!=-1) {
		fromText = true;
	}
	drawingArea.addPoint(50,50);
	drawingArea.addPoint(50,1150);
	drawingArea.addPoint(1150,1150);
	drawingArea.addPoint(1150,50);
	drawingArea.addPoint(50,50);
	
	String inputFolder = rootDir;
	initializeGeometry();
	
	Background background = Background.BYTEGAN40;
	for (int i= 0;i<Background.values().length;i++) {
		background = Background.values()[i];	
		switch(background) {
	    case BYTEGAN20:
	    	double by10 = 10;
    		possibilities = 3;
    		shapestartindex = 16;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background, by10);
				createLineArtOutsideRawPolygon(background, by10);
			}
			if (fromPaths) {
					createLineArtFromMinifiedPaths(background, by10);
			}
			break;
		case BYTEGAN60:
    		double by60 = 3;
    		possibilities = 3;
    		shapestartindex = 16;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background,by60);
				createLineArtOutsideRawPolygon(background,by60);
			}
			if (fromPaths) {
				createLineArtFromMinifiedPaths(background,by60);
			}
			break;
		
		case HEX20:
	    	double h20 = 6;
			possibilities = 16;
			shapestartindex = 0;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background, h20);
				createLineArtOutsideRawPolygon(background, h20);
			} 
			if (fromText) {
					createLineArtFromText(background,h20, false);
			}
			if (fromPaths) {
					createLineArtFromMinifiedPaths(background, h20);
			}
			break;
	    case BINARY20:
	    	double b20 = 6;
    		possibilities = 2;
    		shapestartindex = 0;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background, b20);
				createLineArtOutsideRawPolygon(background, b20);
			}
			if (fromText) {
					createLineArtFromText(background, b20, false);
			}
			if (fromPaths) {
					createLineArtFromMinifiedPaths(background, b20);
			}
			break;
		case BINARY150:
			double b150 = 1;
			possibilities = 2;
			shapestartindex = 0;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background,b150);
				createLineArtOutsideRawPolygon(background,b150);
			}
			if (fromPaths) {
				createLineArtFromMinifiedPaths(background,b150);
			}
	        break;	
		case BINARY100:
			double b100 = 1.5;
			possibilities = 2;
			shapestartindex = 0;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background,b100);
				createLineArtOutsideRawPolygon(background,b100);
			}
			if (fromPaths) {
				createLineArtFromMinifiedPaths(background,b100);
			}
	        break;	
		case BINARY75:
        	double b75 = 2;
			possibilities = 2;
			shapestartindex = 0;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background,b75);
				createLineArtOutsideRawPolygon(background,b75);
			}
			if (fromPaths) {
				createLineArtFromMinifiedPaths(background,b75);
			}
	        break;	        
	        
        case BINARY50:
        	double b50 = 3;
			possibilities = 2;
			shapestartindex = 0;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background,b50);
				createLineArtOutsideRawPolygon(background,b50);
			}
			if (fromPaths) {
				createLineArtFromMinifiedPaths(background,b50);
			}
	        break;
        case BINARY37:
        	double b37 = 4;
			possibilities = 2;
			shapestartindex = 0;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background,b37);
				createLineArtOutsideRawPolygon(background,b37);
			}
			if (fromPaths) {
				createLineArtFromMinifiedPaths(background,b37);
			}

	        break;
	    case HEX40:
	    	double h40 = 4;
			possibilities = 16;
			shapestartindex = 0;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background,h40);
				createLineArtOutsideRawPolygon(background,h40);
			}
			if (fromPaths) {
				createLineArtFromMinifiedPaths(background,h40);
			}
			if (fromText) {
				createLineArtFromText(background, h40, false);
			}
			
	        break;
	    case HEX60:
	    	double h60 = 3;
			possibilities = 16;
			shapestartindex = 0;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background,h60);
				createLineArtOutsideRawPolygon(background,h60);
			}
			if (fromPaths) {
				createLineArtFromMinifiedPaths(background,h60);
			}
			if (fromText) {
				createLineArtFromText(background, h60, false);
			}
			
	        break;

	    case HEX80:
	    	double h80 = 2;
			possibilities = 16;
			shapestartindex = 0;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background,h80);
				createLineArtOutsideRawPolygon(background,h80);
			}
			if (fromPaths) {
				createLineArtFromMinifiedPaths(background,h80);
			}
			
			
	        break;

	    case BYTEGANTEXT:
	    	double byT = 8;
    		possibilities = 3;
    		shapestartindex = 16;
			if (fromText) {
					createLineArtFromText(background, byT, true);
			}
			break;
	    case BYTEGAN40:
	    	double by40 = 5;
			possibilities = 3;
			shapestartindex = 16;
			if (fromPolygons) {
				createLineArtInsideRawPolygon(background,by40);
				createLineArtOutsideRawPolygon(background,by40);
			}
			if (fromPaths) {
					createLineArtFromMinifiedPaths(background,by40);
			}
			break;

	}
		
	}	

	//set clipmask
}

public static String getLineArtFromText(String svg, Background background, double thisscale, boolean isbytegan) {
	String clipFileString = svg;

	String polygonpaths = "";
	
	//initilize polygons vector
	StringTokenizer stp = new StringTokenizer(clipFileString,"<");
	while (stp.hasMoreTokens()) {
		String thisT = stp.nextToken();
		if (thisT.indexOf("text ")!=-1) {
			thisT = "<"+thisT;
			thisT = thisT.substring(thisT.indexOf("x=")+3);
			String xString = thisT.substring(0,thisT.indexOf("\""));
			thisT = thisT.substring(thisT.indexOf("y=")+3);
			String yString = thisT.substring(0,thisT.indexOf("\""));
			thisT = thisT.substring(thisT.indexOf("\">")+2);
			int characters = thisT.length();
			if (isbytegan) {
				characters = characters-1;
			}
			allPolygons.add( xString+" "+yString+" "+characters );
		}
	}
	
	StringBuffer svgString = new StringBuffer();
	svgString.append("<svg height=\"1200\" width=\"1200\" \n");
	svgString.append("xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"> \n");
	String thisgStyle = "style=\"stroke:#000000;stroke-width:0.5;fill:none\"";
	svgString.append("<g "+thisgStyle+">\n");
	//svgString.append("<rect x=\"0\" y=\"0\" width=\"1200\" height=\"1200\" />\n");
	//svgString.append("<rect x=\"50\" y=\"50\" width=\"1100\" height=\"1100\" />\n");
	StringBuffer allshapes = new StringBuffer();;
	for (int p = 0;p<allPolygons.size();p++) {
		allshapes.append( getTextFromCoords( ((String)allPolygons.elementAt(p)),thisscale ) );
	}
	svgString.append(allshapes);
	//svgString.append("<path d=\""+polygonpaths+"\"/>");
	svgString.append("</g></svg>");
	//saveSvgString(clipFile.substring(0,clipFile.indexOf("."))+"_"+background+"_clipped.svg", svgString.toString());
	allPolygons = new Vector();

	return(svgString.toString());
}


public static void createLineArtFromText(Background background, double thisscale, boolean isbytegan) {
	String clipFileString = readInFileToString( rootDir+clipFile );

	String polygonpaths = "";
	
	//initilize polygons vector
	StringTokenizer stp = new StringTokenizer(clipFileString,"<");
	while (stp.hasMoreTokens()) {
		String thisT = stp.nextToken();
		if (thisT.indexOf("text ")!=-1) {
			thisT = "<"+thisT;
			thisT = thisT.substring(thisT.indexOf("x=")+3);
			String xString = thisT.substring(0,thisT.indexOf("\""));
			thisT = thisT.substring(thisT.indexOf("y=")+3);
			String yString = thisT.substring(0,thisT.indexOf("\""));
			thisT = thisT.substring(thisT.indexOf("\">")+2);
			int characters = thisT.length();
			if (isbytegan) {
				characters = characters-1;
			}
			allPolygons.add( xString+" "+yString+" "+characters );
		}
	}
	
	StringBuffer svgString = new StringBuffer();
	svgString.append("<svg height=\"1200\" width=\"1200\" \n");
	svgString.append("xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"> \n");
	String thisgStyle = "style=\"stroke:#000000;stroke-width:0.5;fill:none\"";
	svgString.append("<g "+thisgStyle+">\n");
	svgString.append("<rect x=\"0\" y=\"0\" width=\"1200\" height=\"1200\" />\n");
	svgString.append("<rect x=\"50\" y=\"50\" width=\"1100\" height=\"1100\" />\n");
	StringBuffer allshapes = new StringBuffer();;
	for (int p = 0;p<allPolygons.size();p++) {
		allshapes.append( getTextFromCoords( ((String)allPolygons.elementAt(p)),thisscale ) );
	}
	svgString.append(allshapes);
	//svgString.append("<path d=\""+polygonpaths+"\"/>");
	svgString.append("</g></svg>");
	saveSvgString(clipFile.substring(0,clipFile.indexOf("."))+"_"+background+"_clipped.svg", svgString.toString());

	allPolygons = new Vector();
}

public static String getTextFromCoords(String x_y_characters, double textscale) {
	StringTokenizer st = new StringTokenizer(x_y_characters);
	int x = (int)(Double.parseDouble(st.nextToken()));
	int y = (int)(Double.parseDouble(st.nextToken()));
	int characters = Integer.parseInt(st.nextToken());
	
	StringBuffer allPoints = new StringBuffer();
	
	Random rand = new Random();
	int randint = 0;
	int currentShift = 0;
	for (int c=0;c<characters;c++) {
		randint = rand.nextInt(possibilities)+shapestartindex;
		String startPath = "<path d=\"M ";
	    String endPath = "z\"/>\n";
		Vector points = (Vector) shapeGeometry.elementAt(randint);
		double charwidth = ((double)((int)shapeWidth.elementAt(randint))+1)*textscale;
		double charheight = ((double)((int)shapeHeight.elementAt(randint))+1)*textscale;
				for (int i=0; i<points.size();i++) {
					if ( ((Point)points.elementAt(i)).getX()==-1.0 ) {
						allPoints.append(startPath);
					} else if (((Point)points.elementAt(i)).getX()==-2.0 ) {
						allPoints.append(endPath);
					} else {
						double tx = x+currentShift+(((Point)points.elementAt(i)).getX()*textscale);
						double ty = y-charheight+(((Point)points.elementAt(i)).getY()*textscale);
						allPoints.append(tx+","+ty+" ");
					}
				}
				currentShift += charwidth;
	}
	String returnString = allPoints.toString();
	return returnString;
}

public static String getLineArtInsideRawPolygon(String rawPolygon, Background background, double thisscale, boolean fixed) {
	String clipFileString = rawPolygon;
	//Polygon polygonClip = getPolygonFromString(clipFileString);
	StringBuffer svgString = new StringBuffer();
	svgString.append("<svg height=\"1200\" width=\"1200\" \n");
	svgString.append("xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"> \n");
	String thisgStyle = "style=\"stroke:#000000;stroke-width:0.5;fill:none\"";
	svgString.append("<g "+thisgStyle+">\n");
	//svgString.append("<rect x=\"0\" y=\"0\" width=\"1200\" height=\"1200\" />\n");
	//svgString.append("<rect x=\"50\" y=\"50\" width=\"1100\" height=\"1100\" />\n");
	if (!fixed) {
		svgString.append(getBackgroundPattern(clipFileString,thisscale));
	} else {
		svgString.append(getFixedBackgroundPattern(clipFileString,thisscale));
	}
	svgString.append("</g></svg>");
	return svgString.toString();
}

public static String getLineArtOutsideRawPolygon(String rawPolygon,  Background background, double thisscale) {
	String clipFileString = rawPolygon;
	//Polygon polygonClip = getPolygonFromString(clipFileString);
	StringBuffer svgString = new StringBuffer();
	svgString.append("<svg height=\"1200\" width=\"1200\" \n");
	svgString.append("xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"> \n");
	String thisgStyle = "style=\"stroke:#000000;stroke-width:0.5;fill:none\"";
	svgString.append("<g "+thisgStyle+">\n");
	//svgString.append("<rect x=\"0\" y=\"0\" width=\"1200\" height=\"1200\" />\n");
	//svgString.append("<rect x=\"50\" y=\"50\" width=\"1100\" height=\"1100\" />\n");
	svgString.append(getBackgroundOutsidePattern(clipFileString,thisscale));
	svgString.append("</g></svg>");
	return svgString.toString();

}




public static String getLineArtFromMinifiedPaths(String clip, Background background, double thisscale) {
	String clipFileString = clip;

	String polygonpaths = "";
	
	//initilize polygons vector
	StringTokenizer stp = new StringTokenizer(clipFileString,"<");
	while (stp.hasMoreTokens()) {
		String thisT = stp.nextToken();
		if (thisT.indexOf(" d=\"m")!=-1 || thisT.indexOf(" d=\"M")!=-1 || thisT.indexOf(" M")!=-1) {
			thisT = "<"+thisT;
			allPolygons.add( SVGPathToPolygon.convertSVGPathToPolygon(thisT) );
		}
	}
	
	StringBuffer svgString = new StringBuffer();
	svgString.append("<svg height=\"1200\" width=\"1200\" \n");
	svgString.append("xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"> \n");
	String thisgStyle = "style=\"stroke:#000000;stroke-width:0.5;fill:none\"";
	svgString.append("<g "+thisgStyle+">\n");
	//svgString.append("<rect x=\"0\" y=\"0\" width=\"1200\" height=\"1200\" />\n");
	//svgString.append("<rect x=\"50\" y=\"50\" width=\"1100\" height=\"1100\" />\n");
	StringBuffer allshapes = new StringBuffer();;
	for (int p = 0;p<allPolygons.size();p++) {
		Polygon polygonClip = (Polygon)allPolygons.elementAt(p);
		String polygonsvg = convertPolygonToSVGPolygon(polygonClip);
		polygonpaths = polygonsvg;
		//svgString.append(polygonpaths);
		allshapes.append( getBackgroundPattern(polygonsvg, thisscale) );
	}
	svgString.append(allshapes);
	svgString.append("</g></svg>");
	allPolygons = new Vector();

	return svgString.toString();
}





public static void createLineArtFromMinifiedPaths(Background background, double thisscale) {
	String clipFileString = readInFileToString( rootDir+clipFile );

	String polygonpaths = "";
	
	//initilize polygons vector
	StringTokenizer stp = new StringTokenizer(clipFileString,"<");
	while (stp.hasMoreTokens()) {
		String thisT = stp.nextToken();
		if (thisT.indexOf(" d=\"m")!=-1 || thisT.indexOf(" d=\"M")!=-1) {
			thisT = "<"+thisT;
			allPolygons.add( SVGPathToPolygon.convertSVGPathToPolygon(thisT) );
		}
	}
	
	StringBuffer svgString = new StringBuffer();
	svgString.append("<svg height=\"1200\" width=\"1200\" \n");
	svgString.append("xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"> \n");
	String thisgStyle = "style=\"stroke:#000000;stroke-width:0.5;fill:none\"";
	svgString.append("<g "+thisgStyle+">\n");
	svgString.append("<rect x=\"0\" y=\"0\" width=\"1200\" height=\"1200\" />\n");
	svgString.append("<rect x=\"50\" y=\"50\" width=\"1100\" height=\"1100\" />\n");
	StringBuffer allshapes = new StringBuffer();;
	for (int p = 0;p<allPolygons.size();p++) {
		Polygon polygonClip = (Polygon)allPolygons.elementAt(p);
		String polygonsvg = convertPolygonToSVGPolygon(polygonClip);
		polygonpaths = polygonsvg;
		svgString.append(polygonpaths);
		allshapes.append( getBackgroundPattern(polygonsvg, thisscale) );
	}
	svgString.append(allshapes);
	svgString.append("</g></svg>");
	saveSvgString(clipFile.substring(0,clipFile.indexOf("."))+"_"+background+"_inside_paths.svg", svgString.toString());

	allPolygons = new Vector();
}


public static void createLineArtInsideRawPolygon(Background background, double thisscale) {
	String clipFileString = readInFileToString( rootDir+clipFile );
	//Polygon polygonClip = getPolygonFromString(clipFileString);
	StringBuffer svgString = new StringBuffer();
	svgString.append("<svg height=\"1200\" width=\"1200\" \n");
	svgString.append("xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"> \n");
	String thisgStyle = "style=\"stroke:#000000;stroke-width:0.5;fill:none\"";
	svgString.append("<g "+thisgStyle+">\n");
	svgString.append("<rect x=\"0\" y=\"0\" width=\"1200\" height=\"1200\" />\n");
	svgString.append("<rect x=\"50\" y=\"50\" width=\"1100\" height=\"1100\" />\n");
	svgString.append(getBackgroundPattern(clipFileString,thisscale));
	svgString.append("</g></svg>");
	saveSvgString(clipFile.substring(0,clipFile.indexOf("."))+"_"+background+"_inside_polygons.svg", svgString.toString());
}

public static void createLineArtOutsideRawPolygon(Background background, double thisscale) {
	String clipFileString = readInFileToString( rootDir+clipFile );
	//Polygon polygonClip = getPolygonFromString(clipFileString);
	StringBuffer svgString = new StringBuffer();
	svgString.append("<svg height=\"1200\" width=\"1200\" \n");
	svgString.append("xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"> \n");
	String thisgStyle = "style=\"stroke:#000000;stroke-width:0.5;fill:none\"";
	svgString.append("<g "+thisgStyle+">\n");
	svgString.append("<rect x=\"0\" y=\"0\" width=\"1200\" height=\"1200\" />\n");
	svgString.append("<rect x=\"50\" y=\"50\" width=\"1100\" height=\"1100\" />\n");
	svgString.append(getBackgroundOutsidePattern(clipFileString,thisscale));
	svgString.append("</g></svg>");
	saveSvgString(clipFile.substring(0,clipFile.indexOf("."))+"_"+background+"_outside_polygons.svg", svgString.toString());

}


public static String convertPolygonToSVGPolygon(Polygon polygon) {
    if (polygon == null || polygon.npoints == 0) {
        return "";
    }

    StringBuilder svgPath = new StringBuilder();
    svgPath.append("<polygon points=\"");

    for (int i = 1; i < polygon.npoints; i++) {
        svgPath.append(" ").append(polygon.xpoints[i]).append(" ").append(polygon.ypoints[i]);
    }

    svgPath.append("\"/>"); // Close the path
    return svgPath.toString();
}


public static String convertPolygonToSVGPath(Polygon polygon) {
    if (polygon == null || polygon.npoints == 0) {
        return "";
    }

    StringBuilder svgPath = new StringBuilder();
    svgPath.append("M").append(polygon.xpoints[0]).append(",").append(polygon.ypoints[0]).append(" ");

    for (int i = 1; i < polygon.npoints; i++) {
        svgPath.append("L").append(polygon.xpoints[i]).append(",").append(polygon.ypoints[i]).append(" ");
    }

    svgPath.append("Z"); // Close the path
    return svgPath.toString();
}

public static Vector<Polygon> getPolygonsFromString(String polygon) {
	
	//pindar
	
	Vector polygonpaths = new Vector();;
	Vector polys = new Vector();
	
	//initilize polygons vector
	StringTokenizer stp = new StringTokenizer(polygon,"<");
	while (stp.hasMoreTokens()) {
		String thisT = stp.nextToken();
		if (thisT.indexOf("points=\"")!=-1) {
			thisT = "<"+thisT;
			polygonpaths.add( thisT );
		}
	}

	for (int p=0;p<polygonpaths.size();p++) {
		String thisp = (String)polygonpaths.elementAt(p);
		thisp = thisp.substring(thisp.indexOf("points=")+8);
		thisp = thisp.substring(0,thisp.indexOf("\""));
		StringTokenizer st = new StringTokenizer(thisp," ");
		Polygon thisPolygon = new Polygon();
		while (st.hasMoreTokens()) {
			String xtoken= st.nextToken();
			String ytoken= st.nextToken();
			int nextX = (int)(Double.parseDouble(xtoken));
			int nextY = (int)(Double.parseDouble(ytoken));
			if(nextX !=0 && nextY!=0) {
				thisPolygon.addPoint(nextX,nextY);
			}
		}
		polys.add(thisPolygon);
		System.out.println();
	}
	
	
	
	return polys;
}


public static Polygon getPolygonFromString(String polygon) {
	
	StringTokenizer st = new StringTokenizer(polygon," ");
	Polygon thisPolygon = new Polygon();
	System.out.println("Polygon:"+polygon);
	while (st.hasMoreTokens()) {
		String xtoken= st.nextToken();
		String ytoken= st.nextToken();
		int nextX = (int)(Double.parseDouble(xtoken));
		int nextY = (int)(Double.parseDouble(ytoken));
		if(nextX !=0 && nextY!=0) {
			thisPolygon.addPoint(nextX,nextY);
		}
	}
	
	for(int i=0;i<thisPolygon.npoints;i++ ) {
	 System.out.print(thisPolygon.xpoints[i]+" "+thisPolygon.ypoints[i]+" ");	
	}
	System.out.println();
	return thisPolygon;
}

public static String getBackgroundPattern(String polygonClip, double thisscale) {

	Vector<Polygon> thepolys = getPolygonsFromString(polygonClip);
	
	StringBuffer paths = new StringBuffer();
	
	int startX = 0;
	int startY = 0;
	int maxX = 1200;
	int maxY = 1200;

	double thisXStep = 0;
	double thisYStep = 0;
	
	Random rand = new Random();
	int randint = 0;
	for (double iy=startY;iy<maxY;iy=iy+thisYStep) {
		for (double ix=startX;ix<maxX;ix=ix+thisXStep) {
	        // Generate random integers in range 0 to 16
			randint = rand.nextInt(possibilities)+shapestartindex;
			for (int pp=0;pp<thepolys.size();pp++) {
				int centerx = (int)(thisscale*((int)((((int)shapeWidth.elementAt(randint)))))/2);
				int centery = (int)(thisscale*((int)((((int)shapeHeight.elementAt(randint)))))/2);
				if (drawingArea.contains(
						ix+(centerx*thisscale),
						iy+(centery*thisscale)
						)) {
					paths.append(getPathsIfCenterIsInside(randint, ix, iy, thisscale, thepolys.elementAt(pp)));
				}
			}
			thisXStep = ((int)shapeWidth.elementAt(randint)+1)*thisscale;
		}		
		thisYStep = ((int)shapeHeight.elementAt(randint)+1)*thisscale;
	}
	return(paths.toString());
}


public static String getFixedBackgroundPattern(String polygonClip, double thisscale) {

	Vector<Polygon> thepolys = getPolygonsFromString(polygonClip);
	
	StringBuffer paths = new StringBuffer();
	
	int startX = 0;
	int startY = 0;
	int maxX = 1200;
	int maxY = 1200;

	double thisXStep = 60;
	double thisYStep = 60;
	
	Random rand = new Random();
	int randint = 0;
	for (double iy=startY;iy<maxY;iy=iy+thisYStep) {
		for (double ix=startX;ix<maxX;ix=ix+thisXStep) {
	        // Generate random integers in range 0 to 16
			randint = rand.nextInt(possibilities)+shapestartindex;
			for (int pp=0;pp<thepolys.size();pp++) {
				
				if (drawingArea.contains(
						ix,
						iy
						)) {
					paths.append(getPathsIfCenterIsInside(randint, ix, iy, thisscale, thepolys.elementAt(pp)));
				}
			}
//			thisXStep = ((int)shapeWidth.elementAt(randint)+1)*thisscale;
		}		
//		thisYStep = ((int)shapeHeight.elementAt(randint)+1)*thisscale;
	}
	return(paths.toString());
}


public static String getBackgroundOutsidePattern(String polygonClip, double thisscale) {

	Vector<Polygon> thepolys = getPolygonsFromString(polygonClip);
	
	StringBuffer paths = new StringBuffer();
	
	int startX = 0;
	int startY = 0;
	int maxX = 1200;
	int maxY = 1200;

	double thisXStep = 0;
	double thisYStep = 0;
	
	Random rand = new Random();
	int randint = 0;
	for (double iy=startY;iy<maxY;iy=iy+thisYStep) {
		for (double ix=startX;ix<maxX;ix=ix+thisXStep) {
	        // Generate random integers in range 0 to 16
			randint = rand.nextInt(possibilities)+shapestartindex;
			int centerx = (int)(thisscale*((int)((((int)shapeWidth.elementAt(randint)))))/2);
			int centery = (int)(thisscale*((int)((((int)shapeHeight.elementAt(randint)))))/2);

				if (drawingArea.contains(
						ix+centerx, 
						iy+centery
				)) {
					paths.append(getPathsOutside(randint, ix, iy, thisscale, thepolys));
				}
				
			thisXStep = ((int)shapeWidth.elementAt(randint)+1)*thisscale;
		}		
		thisYStep = ((int)shapeHeight.elementAt(randint)+1)*thisscale;
	}
	return(paths.toString());//+"<path clip-rule=\"evenodd\" d=\"M300 360 300 720 420 720 420 840 540 840 540 720 660 720 660 840 780 840 780 720 900 720 900 360 780 360 780 240 420 240 420 360 300 360z M420 480 420 600 540 600 540 480 420 480z M660 480 660 600 780 600 780 480 660 480z\"/>");
}





public static String getPathsIfCenterIsInside(int gindex, double xx, double yy, double thisscale2, Polygon poly) {

	boolean allInside = true;
	String returnString = "";
	StringBuffer allPoints = new StringBuffer();
	
	String startPath = "<path d=\"M ";
    String endPath = "z\"/>\n";

	Vector points = (Vector) shapeGeometry.elementAt(gindex);
	
			for (int i=0; i<points.size();i++) {
				if ( ((Point)points.elementAt(i)).getX()==-1.0 ) {
					allPoints.append(startPath);
				} else if (((Point)points.elementAt(i)).getX()==-2.0 ) {
					allPoints.append(endPath);
				} else {
					double tx = xx+(((Point)points.elementAt(i)).getX()*thisscale2);
					double ty = yy+(((Point)points.elementAt(i)).getY()*thisscale2);
					allPoints.append(tx+","+ty+" ");
				}
			}
			returnString = allPoints.toString();

		//if not in polygon - ignore it	
		
		int centerX = (int)((((int)shapeWidth.elementAt(gindex))*thisscale2)/2);
		int centerY = (int)((((int)shapeHeight.elementAt(gindex))*thisscale2)/2);
			
		if ( !poly.contains(xx+centerX ,yy+centerY )) {
			returnString ="";
		}
			
    
	return returnString;
}


public static String getPathsOutside(int gindex, double ix, double iy, double thisscale2, Vector thepolys) {

	boolean anyinside = false;
	String returnString = "";
	StringBuffer allPoints = new StringBuffer();
	
	String startPath = "<path d=\"M ";
    String endPath = "z\"/>\n";

	Vector points = (Vector) shapeGeometry.elementAt(gindex);
	
			for (int i=0; i<points.size();i++) {
				if ( ((Point)points.elementAt(i)).getX()==-1.0 ) {
					allPoints.append(startPath);
				} else if (((Point)points.elementAt(i)).getX()==-2.0 ) {
					allPoints.append(endPath);
				} else {
					double tx = ix+(((Point)points.elementAt(i)).getX()*thisscale2);
					double ty = iy+(((Point)points.elementAt(i)).getY()*thisscale2);
					allPoints.append(tx+","+ty+" ");
					for (int pp=0;pp<thepolys.size();pp++) {
						if (  ((Polygon)thepolys.elementAt(pp)).contains(tx, ty)) {
							anyinside = true;
						}
					}
				}
			}
    returnString = allPoints.toString();
    if (anyinside) {
    	returnString = "";
    } else {
    	System.out.println("HERE:"+returnString);
    }
	return returnString;
}

public static String getPaths(int gindex, double xx, double yy, double thisscale2, Polygon poly) {

	boolean allInside = true;
	String returnString = "";
	StringBuffer allPoints = new StringBuffer();
	
	String startPath = "<path d=\"M ";
    String endPath = "z\"/>\n";

	Vector points = (Vector) shapeGeometry.elementAt(gindex);
	
			for (int i=0; i<points.size();i++) {
				if ( ((Point)points.elementAt(i)).getX()==-1.0 ) {
					allPoints.append(startPath);
				} else if (((Point)points.elementAt(i)).getX()==-2.0 ) {
					allPoints.append(endPath);
				} else {
					double tx = xx+(((Point)points.elementAt(i)).getX()*thisscale2);
					double ty = yy+(((Point)points.elementAt(i)).getY()*thisscale2);
					allPoints.append(tx+","+ty+" ");
					if (!poly.contains(tx, ty)) {
						allInside = false;
					}
				}
			}
    returnString = allPoints.toString();
    
    	if (!allInside) {
    		returnString = "";
    	} else {
    		System.out.println(returnString);
    	}
    
	return returnString;
}


public static void initializeGeometry() {
	
	
	//0
	Vector shapePoints0 = new Vector();
	shapePoints0.add(new Point(-1,-1));
	shapePoints0.add(new Point(0,1));
	shapePoints0.add(new Point(0,6));
	shapePoints0.add(new Point(1,6));
	shapePoints0.add(new Point(1,7));
	shapePoints0.add(new Point(4,7));
	shapePoints0.add(new Point(4,6));
	shapePoints0.add(new Point(5,6));
	shapePoints0.add(new Point(5,1));
	shapePoints0.add(new Point(4,1));
	shapePoints0.add(new Point(4,0));
	shapePoints0.add(new Point(1,0));
	shapePoints0.add(new Point(1,6));
	shapePoints0.add(new Point(4,6));
	shapePoints0.add(new Point(4,1));
	shapePoints0.add(new Point(0,1));
	shapePoints0.add(new Point(-2,-2));
	shapeGeometry.add(shapePoints0);
	shapeWidth.add(5);
	shapeHeight.add(7);

	//1
	Vector shapePoints1 = new Vector();
	shapePoints1.add(new Point(-1,-1));
	shapePoints1.add(new Point(0,0));
	shapePoints1.add(new Point(0,1));
	shapePoints1.add(new Point(1,1));
	shapePoints1.add(new Point(1,6));
	shapePoints1.add(new Point(0,6));
	shapePoints1.add(new Point(0,7));
	shapePoints1.add(new Point(3,7));
	shapePoints1.add(new Point(3,6));
	shapePoints1.add(new Point(2,6));
	shapePoints1.add(new Point(2,0));
	shapePoints1.add(new Point(0,0));
	shapePoints1.add(new Point(-2,-2));
	shapeGeometry.add(shapePoints1);
	shapeWidth.add(3);
	shapeHeight.add(7);

	//2
	Vector shapePoints2 = new Vector();
	shapePoints2.add(new Point(-1,-1));
	shapePoints2.add(new Point(0,1));
	shapePoints2.add(new Point(0,2));
	shapePoints2.add(new Point(1,2));
	shapePoints2.add(new Point(1,1));
	shapePoints2.add(new Point(4,1));
	shapePoints2.add(new Point(4,3));
	shapePoints2.add(new Point(2,3));
	shapePoints2.add(new Point(2,4));
	shapePoints2.add(new Point(1,4));
	shapePoints2.add(new Point(1,5));
	shapePoints2.add(new Point(0,5));
	shapePoints2.add(new Point(0,7));
	shapePoints2.add(new Point(5,7));
	shapePoints2.add(new Point(5,6));
	shapePoints2.add(new Point(1,6));
	shapePoints2.add(new Point(1,5));
	shapePoints2.add(new Point(2,5));
	shapePoints2.add(new Point(2,4));
	shapePoints2.add(new Point(4,4));
	shapePoints2.add(new Point(4,3));
	shapePoints2.add(new Point(5,3));
	shapePoints2.add(new Point(5,1));
	shapePoints2.add(new Point(4,1));
	shapePoints2.add(new Point(4,0));
	shapePoints2.add(new Point(1,0));
	shapePoints2.add(new Point(1,1));
	shapePoints2.add(new Point(0,1));
	shapePoints2.add(new Point(-2,-2));
	shapeGeometry.add(shapePoints2);
	shapeWidth.add(5);
	shapeHeight.add(7);
	
	//3
	Vector shapePoints3 = new Vector();
	shapePoints3.add(new Point(-1,-1));
	shapePoints3.add(new Point(0,1));
	shapePoints3.add(new Point(0,2));
	shapePoints3.add(new Point(1,2));
	shapePoints3.add(new Point(1,1));
	shapePoints3.add(new Point(4,1));
	shapePoints3.add(new Point(4,3));
	shapePoints3.add(new Point(2,3));
	shapePoints3.add(new Point(2,4));
	shapePoints3.add(new Point(4,4));
	shapePoints3.add(new Point(4,6));
	shapePoints3.add(new Point(1,6));
	shapePoints3.add(new Point(1,5));
	shapePoints3.add(new Point(0,5));
	shapePoints3.add(new Point(0,6));
	shapePoints3.add(new Point(1,6));
	shapePoints3.add(new Point(1,7));
	shapePoints3.add(new Point(4,7));
	shapePoints3.add(new Point(4,6));
	shapePoints3.add(new Point(5,6));
	shapePoints3.add(new Point(5,4));
	shapePoints3.add(new Point(4,4));
	shapePoints3.add(new Point(4,3));
	shapePoints3.add(new Point(5,3));
	shapePoints3.add(new Point(5,1));
	shapePoints3.add(new Point(4,1));
	shapePoints3.add(new Point(4,0));
	shapePoints3.add(new Point(1,0));
	shapePoints3.add(new Point(1,1));
	shapePoints3.add(new Point(0,1));
	shapePoints3.add(new Point(-2,-2));
	shapeGeometry.add(shapePoints3);
	shapeWidth.add(5);
	shapeHeight.add(7);
	
	//4
	Vector shapePoints4 = new Vector();
	shapePoints4.add(new Point(-1,-1));
	shapePoints4.add(new Point(0,0));
	shapePoints4.add(new Point(0,4));
	shapePoints4.add(new Point(3,4));
	shapePoints4.add(new Point(3,7));
	shapePoints4.add(new Point(4,7));
	shapePoints4.add(new Point(4,4));
	shapePoints4.add(new Point(5,4));
	shapePoints4.add(new Point(5,3));
	shapePoints4.add(new Point(4,3));
	shapePoints4.add(new Point(4,0));
	shapePoints4.add(new Point(3,0));
	shapePoints4.add(new Point(3,3));
	shapePoints4.add(new Point(1,3));
	shapePoints4.add(new Point(1,0));
	shapePoints4.add(new Point(0,0));
	shapePoints4.add(new Point(-2,-2));
	shapeGeometry.add(shapePoints4);
	shapeWidth.add(5);
	shapeHeight.add(7);
	
	//5
	Vector shapePoints5 = new Vector();
	shapePoints5.add(new Point(-1,-1));
	shapePoints5.add(new Point(0,0));
	shapePoints5.add(new Point(0,3));
	shapePoints5.add(new Point(1,3));
	shapePoints5.add(new Point(1,4));
	shapePoints5.add(new Point(4,4));
	shapePoints5.add(new Point(4,6));
	shapePoints5.add(new Point(1,6));
	shapePoints5.add(new Point(1,5));
	shapePoints5.add(new Point(0,5));
	shapePoints5.add(new Point(0,6));
	shapePoints5.add(new Point(1,6));
	shapePoints5.add(new Point(1,7));
	shapePoints5.add(new Point(4,7));
	shapePoints5.add(new Point(4,6));
	shapePoints5.add(new Point(5,6));
	shapePoints5.add(new Point(5,4));
	shapePoints5.add(new Point(4,4));
	shapePoints5.add(new Point(4,3));
	shapePoints5.add(new Point(1,3));
	shapePoints5.add(new Point(1,1));
	shapePoints5.add(new Point(5,1));
	shapePoints5.add(new Point(5,0));
	shapePoints5.add(new Point(0,0));
	shapePoints5.add(new Point(-2,-2));
	shapeGeometry.add(shapePoints5);
	shapeWidth.add(5);
	shapeHeight.add(7);
	
	//6
	Vector shapePoints6 = new Vector();
	shapePoints6.add(new Point(-1,-1));
	shapePoints6.add(new Point(0,1));
	shapePoints6.add(new Point(0,6));
	shapePoints6.add(new Point(5,6));
	shapePoints6.add(new Point(5,4));
	shapePoints6.add(new Point(1,4));
	shapePoints6.add(new Point(1,7));
	shapePoints6.add(new Point(4,7));
	shapePoints6.add(new Point(4,3));
	shapePoints6.add(new Point(1,3));
	shapePoints6.add(new Point(1,0));
	shapePoints6.add(new Point(4,0));
	shapePoints6.add(new Point(4,2));
	shapePoints6.add(new Point(5,2));
	shapePoints6.add(new Point(5,1));
	shapePoints6.add(new Point(0,1));
	shapePoints6.add(new Point(-2,-2));
	shapeGeometry.add(shapePoints6);
	shapeWidth.add(5);
	shapeHeight.add(7);

	//7
	Vector shapePoints7 = new Vector();
	shapePoints7.add(new Point(-1,-1));
	shapePoints7.add(new Point(0,0));
	shapePoints7.add(new Point(0,2));
	shapePoints7.add(new Point(1,2));
	shapePoints7.add(new Point(1,1));
	shapePoints7.add(new Point(4,1));
	shapePoints7.add(new Point(4,3));
	shapePoints7.add(new Point(3,3));
	shapePoints7.add(new Point(3,5));
	shapePoints7.add(new Point(2,5));
	shapePoints7.add(new Point(2,7));
	shapePoints7.add(new Point(3,7));
	shapePoints7.add(new Point(3,5));
	shapePoints7.add(new Point(4,5));
	shapePoints7.add(new Point(4,3));
	shapePoints7.add(new Point(5,3));
	shapePoints7.add(new Point(5,0));
	shapePoints7.add(new Point(0,0));
	shapePoints7.add(new Point(-2,-2));
	shapeGeometry.add(shapePoints7);
	shapeWidth.add(5);
	shapeHeight.add(7);

	//8
	Vector shapePoints8 = new Vector();
	shapePoints8.add(new Point(-1,-1));
	shapePoints8.add(new Point(0,1));
	shapePoints8.add(new Point(0,3));
	shapePoints8.add(new Point(1,3));
	shapePoints8.add(new Point(1,4));
	shapePoints8.add(new Point(0,4));
	shapePoints8.add(new Point(0,6));
	shapePoints8.add(new Point(1,6));
	shapePoints8.add(new Point(1,7));
	shapePoints8.add(new Point(4,7));
	shapePoints8.add(new Point(4,4));
	shapePoints8.add(new Point(1,4));
	shapePoints8.add(new Point(1,6));
	shapePoints8.add(new Point(5,6));
	shapePoints8.add(new Point(5,4));
	shapePoints8.add(new Point(4,4));
	shapePoints8.add(new Point(4,3));
	shapePoints8.add(new Point(5,3));
	shapePoints8.add(new Point(5,1));
	shapePoints8.add(new Point(4,1));
	shapePoints8.add(new Point(4,0));
	shapePoints8.add(new Point(1,0));
	shapePoints8.add(new Point(1,3));
	shapePoints8.add(new Point(4,3));
	shapePoints8.add(new Point(4,1));
	shapePoints8.add(new Point(0,1));
	shapePoints8.add(new Point(-2,-2));
	shapeGeometry.add(shapePoints8);
	shapeWidth.add(5);
	shapeHeight.add(7);
	
	//9
	Vector shapePoints9 = new Vector();
	shapePoints9.add(new Point(-1,-1));
	shapePoints9.add(new Point(0,1));
	shapePoints9.add(new Point(0,3));
	shapePoints9.add(new Point(1,3));
	shapePoints9.add(new Point(1,4));
	shapePoints9.add(new Point(1,4));
	shapePoints9.add(new Point(4,4));
	shapePoints9.add(new Point(4,6));
	shapePoints9.add(new Point(1,6));
	shapePoints9.add(new Point(1,5));
	shapePoints9.add(new Point(0,5));
	shapePoints9.add(new Point(0,6));
	shapePoints9.add(new Point(1,6));
	shapePoints9.add(new Point(1,7));
	shapePoints9.add(new Point(4,7));
	shapePoints9.add(new Point(4,6));
	shapePoints9.add(new Point(5,6));
	shapePoints9.add(new Point(5,1));
	shapePoints9.add(new Point(4,1));
	shapePoints9.add(new Point(4,0));
	shapePoints9.add(new Point(1,0));
	shapePoints9.add(new Point(1,3));
	shapePoints9.add(new Point(4,3));
	shapePoints9.add(new Point(4,1));
	shapePoints9.add(new Point(0,1));
	shapePoints9.add(new Point(-2,-2));
	shapeGeometry.add(shapePoints9);
	shapeWidth.add(5);
	shapeHeight.add(7);

	//A
	Vector shapePointsA = new Vector();
	shapePointsA.add(new Point(-1,-1));
	shapePointsA.add(new Point(0,1));
	shapePointsA.add(new Point(0,7));
	shapePointsA.add(new Point(1,7));
	shapePointsA.add(new Point(1,4));
	shapePointsA.add(new Point(4,4));
	shapePointsA.add(new Point(4,7));
	shapePointsA.add(new Point(5,7));
	shapePointsA.add(new Point(5,1));
	shapePointsA.add(new Point(4,1));
	shapePointsA.add(new Point(4,0));
	shapePointsA.add(new Point(1,0));
	shapePointsA.add(new Point(1,3));
	shapePointsA.add(new Point(4,3));
	shapePointsA.add(new Point(4,1));
	shapePointsA.add(new Point(0,1));
	shapePointsA.add(new Point(-2,-2));
	shapeGeometry.add(shapePointsA);
	shapeWidth.add(5);
	shapeHeight.add(7);

	//B
	Vector shapePointsB = new Vector();
	shapePointsB.add(new Point(-1,-1));
	shapePointsB.add(new Point(0,0));
	shapePointsB.add(new Point(0,7));
	shapePointsB.add(new Point(4,7));
	shapePointsB.add(new Point(4,6));
	shapePointsB.add(new Point(5,6));
	shapePointsB.add(new Point(5,4));
	shapePointsB.add(new Point(1,4));
	shapePointsB.add(new Point(1,6));
	shapePointsB.add(new Point(4,6));
	shapePointsB.add(new Point(4,1));
	shapePointsB.add(new Point(1,1));
	shapePointsB.add(new Point(1,3));
	shapePointsB.add(new Point(5,3));
	shapePointsB.add(new Point(5,1));
	shapePointsB.add(new Point(4,1));
	shapePointsB.add(new Point(4,0));
	shapePointsB.add(new Point(0,0));
	shapePointsB.add(new Point(-2,-2));
	shapeGeometry.add(shapePointsB);
	shapeWidth.add(5);
	shapeHeight.add(7);
	
	//C
	Vector shapePointsC = new Vector();
	shapePointsC.add(new Point(-1,-1));
	shapePointsC.add(new Point(0,1));
	shapePointsC.add(new Point(0,6));
	shapePointsC.add(new Point(5,6));
	shapePointsC.add(new Point(5,5));
	shapePointsC.add(new Point(4,5));
	shapePointsC.add(new Point(4,7));
	shapePointsC.add(new Point(1,7));
	shapePointsC.add(new Point(1,0));
	shapePointsC.add(new Point(4,0));
	shapePointsC.add(new Point(4,2));
	shapePointsC.add(new Point(5,2));
	shapePointsC.add(new Point(5,1));
	shapePointsC.add(new Point(0,1));
	shapePointsC.add(new Point(-2,-2));
	shapeGeometry.add(shapePointsC);
	shapeWidth.add(5);
	shapeHeight.add(7);

	//D
	Vector shapePointsD = new Vector();
	shapePointsD.add(new Point(-1,-1));
	shapePointsD.add(new Point(0,0));
	shapePointsD.add(new Point(0,7));
	shapePointsD.add(new Point(4,7));
	shapePointsD.add(new Point(4,1));
	shapePointsD.add(new Point(1,1));
	shapePointsD.add(new Point(1,6));
	shapePointsD.add(new Point(5,6));
	shapePointsD.add(new Point(5,1));
	shapePointsD.add(new Point(4,1));
	shapePointsD.add(new Point(4,0));
	shapePointsD.add(new Point(0,0));
	shapePointsD.add(new Point(-2,-2));
	shapeGeometry.add(shapePointsD);
	shapeWidth.add(5);
	shapeHeight.add(7);

	//E
	Vector shapePointsE = new Vector();
	shapePointsE.add(new Point(-1,-1));
	shapePointsE.add(new Point(0,0));
	shapePointsE.add(new Point(0,7));
	shapePointsE.add(new Point(5,7));
	shapePointsE.add(new Point(5,6));
	shapePointsE.add(new Point(1,6));
	shapePointsE.add(new Point(1,4));
	shapePointsE.add(new Point(4,4));
	shapePointsE.add(new Point(4,3));
	shapePointsE.add(new Point(1,3));
	shapePointsE.add(new Point(1,1));
	shapePointsE.add(new Point(5,1));
	shapePointsE.add(new Point(5,0));
	shapePointsE.add(new Point(0,0));
	shapePointsE.add(new Point(-2,-2));
	shapeGeometry.add(shapePointsE);
	shapeWidth.add(5);
	shapeHeight.add(7);
	
	//F
	Vector shapePointsF = new Vector();
	shapePointsF.add(new Point(-1,-1));
	shapePointsF.add(new Point(0,0));
	shapePointsF.add(new Point(0,7));
	shapePointsF.add(new Point(1,7));
	shapePointsF.add(new Point(1,4));
	shapePointsF.add(new Point(4,4));
	shapePointsF.add(new Point(4,3));
	shapePointsF.add(new Point(1,3));
	shapePointsF.add(new Point(1,1));
	shapePointsF.add(new Point(5,1));
	shapePointsF.add(new Point(5,0));
	shapePointsF.add(new Point(0,0));
	shapePointsF.add(new Point(-2,-2));
	shapeGeometry.add(shapePointsF);
	shapeWidth.add(5);
	shapeHeight.add(7);
	
	//skullgan
		Vector shapePointsSkullGan = new Vector();
		shapePointsSkullGan.add(new Point(-1,-1));
		shapePointsSkullGan.add(new Point(0,1));
		shapePointsSkullGan.add(new Point(0,4));
		shapePointsSkullGan.add(new Point(1,4));
		shapePointsSkullGan.add(new Point(1,5));
		shapePointsSkullGan.add(new Point(2,5));
		shapePointsSkullGan.add(new Point(2,4));
		shapePointsSkullGan.add(new Point(3,4));
		shapePointsSkullGan.add(new Point(3,5));
		shapePointsSkullGan.add(new Point(4,5));
		shapePointsSkullGan.add(new Point(4,4));
		shapePointsSkullGan.add(new Point(5,4));
		shapePointsSkullGan.add(new Point(5,1));
		shapePointsSkullGan.add(new Point(4,1));
		shapePointsSkullGan.add(new Point(4,0));
		shapePointsSkullGan.add(new Point(1,0));
		shapePointsSkullGan.add(new Point(1,1));
		shapePointsSkullGan.add(new Point(0,1));
		shapePointsSkullGan.add(new Point(-2,-2));
		shapePointsSkullGan.add(new Point(-1,-1));
		shapePointsSkullGan.add(new Point(1,2));
		shapePointsSkullGan.add(new Point(1,3));
		shapePointsSkullGan.add(new Point(2,3));
		shapePointsSkullGan.add(new Point(2,2));
		shapePointsSkullGan.add(new Point(1,2));
		shapePointsSkullGan.add(new Point(-2,-2));
		shapePointsSkullGan.add(new Point(-1,-1));
		shapePointsSkullGan.add(new Point(3,2));
		shapePointsSkullGan.add(new Point(3,3));
		shapePointsSkullGan.add(new Point(4,3));
		shapePointsSkullGan.add(new Point(4,2));
		shapePointsSkullGan.add(new Point(3,2));
		shapePointsSkullGan.add(new Point(-2,-2));
		shapeGeometry.add(shapePointsSkullGan);
		shapeWidth.add(5);
		shapeHeight.add(5);
		
		//podgan
		Vector shapePointsPodGan = new Vector();
		shapePointsPodGan.add(new Point(-1,-1));
		shapePointsPodGan.add(new Point(0,0));
		shapePointsPodGan.add(new Point(0,4));
		shapePointsPodGan.add(new Point(1,4));
		shapePointsPodGan.add(new Point(1,3));
		shapePointsPodGan.add(new Point(2,3));
		shapePointsPodGan.add(new Point(2,5));
		shapePointsPodGan.add(new Point(3,5));
		shapePointsPodGan.add(new Point(3,3));
		shapePointsPodGan.add(new Point(4,3));
		shapePointsPodGan.add(new Point(4,4));
		shapePointsPodGan.add(new Point(5,4));
		shapePointsPodGan.add(new Point(5,0));
		shapePointsPodGan.add(new Point(0,0));
		shapePointsPodGan.add(new Point(-2,-2));
		shapePointsPodGan.add(new Point(-1,-1));
		shapePointsPodGan.add(new Point(1,1));
		shapePointsPodGan.add(new Point(1,2));
		shapePointsPodGan.add(new Point(2,2));
		shapePointsPodGan.add(new Point(2,1));
		shapePointsPodGan.add(new Point(1,1));
		shapePointsPodGan.add(new Point(-2,-2));
		shapePointsPodGan.add(new Point(-1,-1));
		shapePointsPodGan.add(new Point(3,1));
		shapePointsPodGan.add(new Point(3,2));
		shapePointsPodGan.add(new Point(4,2));
		shapePointsPodGan.add(new Point(4,1));
		shapePointsPodGan.add(new Point(3,1));
		shapePointsPodGan.add(new Point(-2,-2));
		shapeGeometry.add(shapePointsPodGan);
		shapeWidth.add(5);
		shapeHeight.add(5);
		
		//cybergan
		Vector shapePointsCyberGan = new Vector();
		shapePointsCyberGan.add(new Point(-1,-1));
		shapePointsCyberGan.add(new Point(0,1));
		shapePointsCyberGan.add(new Point(0,5));
		shapePointsCyberGan.add(new Point(1,5));
		shapePointsCyberGan.add(new Point(1,4));
		shapePointsCyberGan.add(new Point(2,4));
		shapePointsCyberGan.add(new Point(2,5));
		shapePointsCyberGan.add(new Point(3,5));
		shapePointsCyberGan.add(new Point(3,4));
		shapePointsCyberGan.add(new Point(4,4));
		shapePointsCyberGan.add(new Point(4,5));
		shapePointsCyberGan.add(new Point(5,5));
		shapePointsCyberGan.add(new Point(5,1));
		shapePointsCyberGan.add(new Point(4,1));
		shapePointsCyberGan.add(new Point(4,0));
		shapePointsCyberGan.add(new Point(3,0));
		shapePointsCyberGan.add(new Point(3,1));
		shapePointsCyberGan.add(new Point(0,1));
		shapePointsCyberGan.add(new Point(-2,-2));
		shapePointsCyberGan.add(new Point(-1,-1));
		shapePointsCyberGan.add(new Point(1,2));
		shapePointsCyberGan.add(new Point(1,3));
		shapePointsCyberGan.add(new Point(4,3));
		shapePointsCyberGan.add(new Point(4,2));
		shapePointsCyberGan.add(new Point(1,2));
		shapePointsCyberGan.add(new Point(-2,-2));
		shapeGeometry.add(shapePointsCyberGan);
		shapeWidth.add(5);
		shapeHeight.add(5);
	
}





public static String readInFileToString(String fileName ) {

	BufferedReader reader;
	StringBuilder stringBuilder = new StringBuilder();
	try {
		reader = new BufferedReader(new FileReader(fileName));
		String line = null;
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		// delete the last new line separator
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		reader.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	String content = stringBuilder.toString();
	return content;
}

public static void saveSvgString(String filename, String svgString) {
	 File file = new File(rootDir+"\\"+filename);
	 if (file.exists()) {
		 file.delete();
	 }
	try {
	     FileWriter writer = new FileWriter(rootDir+"\\"+filename, true);
	     writer.write(svgString);
	     writer.close();
	} catch (IOException e) {
			        e.printStackTrace();
	}
			
}


}