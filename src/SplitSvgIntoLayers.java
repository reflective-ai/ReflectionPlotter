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

public class SplitSvgIntoLayers {


public static String svgStart = "<svg height=\"1200\" width=\"1200\"\r\nxmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">";
public static String styles = "<style> .twentyfourrows {   font: bold 60px sans-serif; } .fortyeightrows {   font: bold 30px sans-serif; } .eightyrows {   font: bold 14px sans-serif; } .blackcolor {   fill: #000000;   stroke: #000000; } .whitecolor {   fill: #fafafa;   stroke: #fafafa; } .blacklinecolor {   fill: none;   stroke: #000000; } .whitelinecolor {   fill: none;   stroke: #fafafa; } .blackstrokes {   stroke: #000000;   stroke-width:40;   stroke-linecap:round; } .whitestrokes {   stroke: #fafafa;   stroke-width:40;   stroke-linecap:round; } .darkstrokes {   stroke: #008617;   stroke-width:40;   stroke-linecap:round; } .mediumstrokes {   stroke: #00e427;   stroke-width:30;   stroke-linecap:round; } .lightstrokes {   stroke: #00ff2c;   stroke-width:20;   stroke-linecap:round; } .backgroundfill {   fill: #000000; } .backgroundcolor {   fill: none;   stroke: #00ff2c; } .circlecolor {   fill: none;   stroke: #00ff2c;   stroke-width:2;   stroke-miterlimit:10; } .skullcolor {   fill: none;    stroke: #fafafa; } .highlightcolor{     fill: none;     stroke: #fafafa;     stroke-width: 2; } .finalcolor{   fill: #010101;   stroke: #fafafa;   stroke-width: 1; } </style>";
public static String invasionclip = "<clipPath id=\"invasionclip\">\r\n"
		+ "<rect  x=\"781.3\" y=\"841.5\" width=\"179.5\" height=\"59.4\"/><rect  x=\"720.4\" y=\"900.9\" width=\"180.5\" height=\"59.6\"/><rect  x=\"660.5\" y=\"960.6\" width=\"180.8\" height=\"61.2\"/><rect  x=\"781.3\" y=\"420.8\" width=\"239.9\" height=\"60.1\"/><rect  x=\"781.1\" y=\"360.3\" width=\"180\" height=\"60.5\"/><rect  x=\"600.6\" y=\"120.1\" width=\"240.3\" height=\"60.8\"/><rect  x=\"239.8\" y=\"600.5\" width=\"179.7\" height=\"60.8\"/><rect  x=\"180\" y=\"540.8\" width=\"180.1\" height=\"59.8\"/><rect  x=\"841.5\" y=\"479.6\" width=\"119.1\" height=\"61\"/><rect  x=\"300.2\" y=\"841.3\" width=\"119.4\" height=\"59.7\"/><rect  x=\"239.9\" y=\"240.4\" width=\"240.5\" height=\"60.5\"/><rect  x=\"180.1\" y=\"300.4\" width=\"240.2\" height=\"60.5\"/><rect  x=\"239.8\" y=\"900.9\" width=\"241\" height=\"59.2\"/><rect  x=\"300.2\" y=\"960.2\" width=\"240.4\" height=\"61.6\"/><rect  x=\"660.8\" y=\"180.6\" width=\"240.7\" height=\"59.3\"/><rect  x=\"360.3\" y=\"1021.8\" width=\"119.1\" height=\"60\"/><rect  x=\"360.3\" y=\"180.6\" width=\"180.1\" height=\"60.3\"/><rect  x=\"841.3\" y=\"661.3\" width=\"119.8\" height=\"59.5\"/>\r\n"
		+ "</clipPath>";
public static String svgEnd = "</svg>";

public static String rootDir = "C:\\Users\\pinda\\Documents\\_________angelsanddemons\\______reflexion\\theart\\________567\\";
public static String svgFile = "567.svg";


public static void main(String[] args) {

	
	
	MakeSvgBackgroundsFromClipmask svgPlotter = new MakeSvgBackgroundsFromClipmask();
	

	String svgFileString = readInFileToString( rootDir+svgFile );
	svgFileString = svgFileString.replace("\r", "");
	svgFileString = svgFileString.replace("\n", "");
	
	//byteGAN
	styles = svgFileString.substring(svgFileString.indexOf("<style>"), svgFileString.indexOf("</style>" ,svgFileString.indexOf("<style>"))+8 );
	invasionclip = svgFileString.substring( svgFileString.indexOf("id=\"invasionclip\">"), svgFileString.indexOf("</clipPath>" ,svgFileString.indexOf("id=\"invasionclip\">")) );
	invasionclip = invasionclip.substring(invasionclip.indexOf("<rect"));		
	invasionclip = convertInvasionClipToRawPolygon(invasionclip);		
			
	String bytegan_dims = getbyteGanDims(svgFileString, "<!-- 10:", "-->");
	String bytegan_01 = getSvgTag(svgFileString, "<!-- 4:", "<!--");
	bytegan_01 = getSvgPaths(bytegan_01, "<clipPath id=\"byteganclip\">", "</clipPath>");
	String bytegan_svg = svgPlotter.plotSVG(bytegan_01, "bytegans", bytegan_dims);
	saveSvgString("01_bytegans.svg", bytegan_svg);
	
	//GAN
	String fullGanString =  getSvgTag(svgFileString, "<!-- 11:", "<!--" );
	String blackStrokes = getSvgTag(fullGanString, "<g class=\"blackstrokes\">", "</g>");
	String darkStrokes = getSvgTag(fullGanString, "<g class=\"mediumstrokes\">", "</g>");;
	String lightStrokes = getSvgTag(fullGanString, "<g class=\"lightstrokes\">", "</g>");;
	String gan_02a = svgStart+styles+blackStrokes+"</g>"+svgEnd;
	String gan_02b = svgStart+styles+darkStrokes+"</g>"+svgEnd;;
	String gan_02c = svgStart+styles+lightStrokes+"</g>"+svgEnd;;
	saveSvgString("02_gan_blackstrokes.svg", gan_02a);
	saveSvgString("03_gan_mediumstrokes.svg", gan_02b);
	saveSvgString("04_gan_lightstrokes.svg", gan_02c);
	
	//SKULL
	String skull_dims = getSvgTag(svgFileString, "<g clip-path=\"url(#skullclip)\"", "<!--" );
	skull_dims = getSkullDims(skull_dims, "href=\"#", "\" ");
	String skull_03 = getSvgTag(svgFileString, "<!-- 5:", "<!--" );
	skull_03 = getSvgPaths(skull_03, "<clipPath id=\"skullclip\">", "</clipPath>");
	String skull_svg = svgPlotter.plotSVG(skull_03, "skull", skull_dims);
	saveSvgString("05_skull.svg", skull_svg);
	
	//Background
	String background_dims = getSvgTag(svgFileString, "<!-- 13:", "<!--");
	//background_dims = getSkullDims(background_dims, "href=\"#", "\" ");
	background_dims = getSkullDims(background_dims, "background ", " -->");
	String background_04 = getSvgTag(svgFileString, "<!-- 4:", "<!--");
	background_04 = getSvgPaths(background_04, "<clipPath id=\"byteganclip\">", "</clipPath>");
	background_04 = svgPlotter.plotSVG(background_04, "background", background_dims);
	saveSvgString("06_background.svg", background_04);

	//diffussion
	String fullDifString =  getSvgTag(svgFileString, "<!-- 14:", "<!--" );
	blackStrokes = getSvgTag(fullDifString, "<g class=\"blackstrokes\">", "</g>");
	darkStrokes = getSvgTag(fullDifString, "<g class=\"darkstrokes\">", "</g>");;
	lightStrokes = getSvgTag(fullDifString, "<g class=\"lightstrokes\">", "</g>");;
	String dif_05a = svgStart+styles+blackStrokes+"</g>"+svgEnd;
	String dif_05b = svgStart+styles+darkStrokes+"</g>"+svgEnd;;
	String dif_05c = svgStart+styles+lightStrokes+"</g>"+svgEnd;;
	saveSvgString("07_diff_blackstrokes.svg", dif_05a);
	saveSvgString("08_diff_darkstrokes.svg", dif_05b);
	saveSvgString("09_diff_lightstrokes.svg", dif_05c);
	
	String circles_svg = getSvgTag(svgFileString, "<!-- 15:", "<!--" );
	String circles_06 = svgStart+styles+circles_svg+svgEnd;;
	saveSvgString("10_circles.svg", circles_06);
		
	
	String touchups_med =  getSvgTag(svgFileString, "<!-- 16:", "<!--" );
	String mediumStrokes = getSvgTag(touchups_med, "<g class=\"mediumstrokes\">", "</g>");
	String touchups_07 = svgStart+styles+mediumStrokes+"</g>"+svgEnd;
	saveSvgString("11_touchups_mediumstrokes.svg", touchups_07);

	String touchups_lite =  getSvgTag(svgFileString, "<!-- 17:", "<!--" );
	String liteStrokes = getSvgTag(touchups_lite, "<g class=\"lightstrokes\">", "</g>");
	String touchups_08 = svgStart+styles+liteStrokes+"</g>"+svgEnd;
	saveSvgString("12_touchups_lightstrokes.svg", touchups_08);
	
	
	String shadow_dims = getSvgTag(svgFileString, "<!-- 18:", "<!--" );
	shadow_dims = getSkullDims(shadow_dims, "href=\"#", "\" ");
	String shadow_13 = getSvgTag(svgFileString, "<!-- 7:", "<!--" );
	shadow_13 = getSvgPaths(shadow_13, "<clipPath id=\"shadowclip\">", "</clipPath>");
	String shadow_svg = svgPlotter.plotSVG(shadow_13, "skull", skull_dims);
	saveSvgString("13_blackshadows.svg", shadow_svg);
	
	String highlight_dims = getSvgTag(svgFileString, "<!-- 19:", "<!--" );
	highlight_dims = getSkullDims(highlight_dims, "href=\"#", "\" ");
	String highlight_13 = getSvgTag(svgFileString, "<!-- 6:", "<!--" );
	highlight_13 = getSvgPaths(highlight_13, "<clipPath id=\"highlightclip\">", "</clipPath>");
	String highlight_svg = svgPlotter.plotSVG(highlight_13, "skull", skull_dims);
	saveSvgString("14_whitehighlights.svg", highlight_svg);
	
	//20:
	
	try {
		String skull2_dims = getSvgTag(svgFileString, "<!-- 20:", "<!--" );
		skull2_dims = getSkullDims(skull2_dims, "href=\"#", "\" ");
		String skull2_15 = getSvgTag(svgFileString, "<!-- 5:", "<!--" );
		skull2_15 = getSvgPaths(skull2_15, "<clipPath id=\"skullclip\">", "</clipPath>");
		String skull2_svg = svgPlotter.plotSVG(skull2_15, "skull", skull2_dims);
		saveSvgString("15_skull2.svg", skull2_svg);
	} catch (StringIndexOutOfBoundsException e)	{
		saveSvgString("15_skull2.svg", svgStart+svgEnd);
		System.out.println("No skull flourish");
	}

	//overlay
	String overlay_dims = getOverlayDims(svgFileString, "<!-- 21:", "-->" );
	String overlay_16 = getSvgTag(svgFileString, "<!-- 21:", "<!--" );
	overlay_16 = getSvgPaths(overlay_16, "class=\"finalcolor\"", "</g>");
	String overlay_svg = svgPlotter.plotSVG(invasionclip, "overlay", overlay_dims);
	saveSvgString("16_overlay.svg", overlay_svg);

	
}

public static String getbyteGanDims(String svg, String start, String end) {
	String finalClip = svg.substring(svg.indexOf(start), svg.indexOf(end,svg.indexOf(start) )-1);
	finalClip = finalClip.substring( finalClip.indexOf(" ")+1 );
	finalClip = finalClip.substring( finalClip.indexOf(" ")+1 );
	finalClip = finalClip.substring( finalClip.indexOf(" ")+1 );
	System.out.println("bytegan-dims:"+finalClip);	
	return finalClip;
}

public static String getOverlayDims(String svg, String start, String end) {
	String finalClip = svg.substring(svg.indexOf(start)+start.length(), svg.indexOf(end,svg.indexOf(start) )-1);
	finalClip = finalClip.substring( finalClip.indexOf(" ")+1 );
	finalClip = finalClip.substring( finalClip.indexOf(" ")+1 );
	finalClip = finalClip.substring( finalClip.indexOf(" ")+1 );
	System.out.println("overlay-dims:"+finalClip);	
	return finalClip;
}

public static String getSkullDims(String svg, String start, String end) {
	String finalClip = svg.substring(svg.indexOf(start)+start.length(), svg.indexOf(end,svg.indexOf(start)+start.length() ));
	System.out.println("skull-dims:"+finalClip);	
	return finalClip;
}


public static String getSvgTag(String svg, String start, String end) {
	String finalClip = svg.substring(svg.indexOf(start), svg.indexOf(end, svg.indexOf(start)+1));
	return finalClip;
}

public static String getSvgPaths(String svg, String start, String end) {
	String finalClip = svg.substring(svg.indexOf(start), svg.indexOf(end, svg.indexOf(start)+1));
	finalClip = finalClip.substring( finalClip.indexOf(">")+1);
	System.out.println("path:"+finalClip);
	return finalClip;
}

/*
<!-- 4:5 byteganclip -->
<clipPath id="byteganclip">
<path clip-rule="evenodd" d="
  M300 360 300 840 420 840 420 720 540 720 540 960 660 960 660 720 780 720 780 840 900 840 900 360 780 360 780 240 420 240 420 360 300 360z M420 480 420 600 780 600 780 480 420 480z"/>
</clipPath>
<clipPath id="byteganreverseclip">
<path clip-rule="evenodd" d="M0,0,0,1200,1200,1200,1200,0,0,0z M300 360 300 840 420 840 420 720 540 720 540 960 660 960 660 720 780 720 780 840 900 840 900 360 780 360 780 240 420 240 420 360 300 360z"/>
</clipPath><!-- 
*/
public static String convertInvasionClipToRawPolygon(String invasionclip) {
	StringBuffer rawpolygon = new StringBuffer();
	boolean more = true;
	String allrects = invasionclip;
	while(more) {
		String thisrect = allrects.substring(0,allrects.indexOf("/>")+2);
		double xx = Double.parseDouble(  getTrait(thisrect, "x") );
		double yy = Double.parseDouble(  getTrait(thisrect, "y") );
		double ww = Double.parseDouble(  getTrait(thisrect, "width") );
		double hh = Double.parseDouble(  getTrait(thisrect, "height") );

		rawpolygon.append("<polygon points=\"");
		rawpolygon.append( ""+xx+" "+yy);
		rawpolygon.append( " "+(xx)+" "+(yy+hh));
		rawpolygon.append( " "+(xx+ww)+" "+(yy+hh));
		rawpolygon.append( " "+(xx+ww)+" "+(yy));
		rawpolygon.append( " "+xx+" "+yy);
		rawpolygon.append("\"/>");

		allrects = allrects.substring(allrects.indexOf("/>")+2);
		if (allrects.indexOf("/>") == -1) {
			more = false;
		}
		
	}
	
	return rawpolygon.toString();
}


public static String getTrait(String rect, String trait) {
	int start = 0;
	int end = 0;
	start = rect.indexOf(trait)+trait.length()+2;
	end = rect.indexOf("\"", start);
	return rect.substring(start,end);
}

// <rect  x="781.3" y="841.5" width="179.5" height="59.4"/><rect  x="720.4" y="900.9" width="180.5" height="59.6"/><rect  x="660.5" y="960.6" width="180.8" height="61.2"/><rect  x="781.3" y="420.8" width="239.9" height="60.1"/><rect  x="781.1" y="360.3" width="180" height="60.5"/><rect  x="600.6" y="120.1" width="240.3" height="60.8"/><rect  x="239.8" y="600.5" width="179.7" height="60.8"/><rect  x="180" y="540.8" width="180.1" height="59.8"/><rect  x="841.5" y="479.6" width="119.1" height="61"/><rect  x="300.2" y="841.3" width="119.4" height="59.7"/><rect  x="239.9" y="240.4" width="240.5" height="60.5"/><rect  x="180.1" y="300.4" width="240.2" height="60.5"/><rect  x="239.8" y="900.9" width="241" height="59.2"/><rect  x="300.2" y="960.2" width="240.4" height="61.6"/><rect  x="660.8" y="180.6" width="240.7" height="59.3"/><rect  x="360.3" y="1021.8" width="119.1" height="60"/><rect  x="360.3" y="180.6" width="180.1" height="60.3"/><rect  x="841.3" y="661.3" width="119.8" height="59.5"/>


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