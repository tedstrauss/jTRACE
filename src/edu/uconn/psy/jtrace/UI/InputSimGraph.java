/*
 * InputSimGraph.java
 *
 * Created on May 11, 2005, 11:33 AM
 */

package edu.uconn.psy.jtrace.UI;

import java.awt.*;
import java.awt.geom.*;
import edu.uconn.psy.jtrace.Model.*;

/**
 *
 * @author harlan
 */
public class InputSimGraph extends AbstractSimGraph 
{
    public InputSimGraph(TraceParam _tp, double [][] _d, int _n, int _i,
            double _min, double _max) 
    {
        super(_tp, _d, _n, _i, _min, _max);
                
        xAxisLabel = "Time";
        yAxisLabel = "Feature Continua";
        title = "Model Input";
        
    }
    
    /**
     * Plot just the stuff inside the box. Over-rides abstract to include the
     * funky blue line
     *
     * @param plotRect      rectangle defining the relative location of the box in the panel
     * @param g2            graphics context
     */
    protected void plotData(Rectangle plotRect, Graphics2D g2)
    {
        if (data == null)
            return;
        
        double boxHeight = plotRect.getHeight() / rows;
        double boxWidth = plotRect.getWidth() / cols;
        for (int row = 0; row < data.length; row++)
        {
            int yPos = (int)Math.round(plotRect.getY() + (row * boxHeight));
            
            for (int col = 0; col < data[row].length; col++)
            {
                int xPos = (int)Math.round(plotRect.getX() + (col * boxWidth));
            
                // compute value between 0 and 255
                int value;
                
                if (data[row][col] < 0) 
                    value = 0;       // threshold at 0
                else
                    value = (int)Math.round(255f * (data[row][col] - minVal) / (maxVal - minVal));
                
                // plot differently depending on where we are relative to the current input time step
                Color c;
                if (col < (currFrame))
                {
                    // plot normally
                    // construct a greyscale color
                    c = new Color((255-value), (255-value), (255-value));
                }
                else if (col == (currFrame))
                {
                    // plot in color
                    c = new Color(value, 200, 200);
                }
                else // col > (currFrame*interval)
                {
                    // just draw a black box
                    c = Color.BLACK;
                }
                
                g2.setColor(c);
                g2.fill(new Rectangle(xPos, yPos, (int)(boxWidth+1), (int)(boxHeight+1)));       
            }
        }
    }
    /** 
     * Over-ride abstract method. This is identical to FeatureSimGraph.plotTicks()
     */
    protected void plotTicks(Rectangle plotRect, Graphics2D g2)
    {
        int tickLength = (int)(szSmallBuf * .7);
        
        double boxHeight = plotRect.getHeight() / rows;
        double boxWidth = plotRect.getWidth() / cols;
       
        g2.setColor(Color.BLACK);
        g2.setFont(fTickLabel);
        
        // foreach row, plot a tick mark. 
        // foreach col, plot a tick mark.
        // and plot 15 X tick labels
        
        // Y tick labels are difficult:
        // for each type of feature, plot a label and edges
        String[] fts={"POW", "VOC", "DIF", "ACU", "GRD", "VOI", "BUR"};
            
        
        // plot if col % colLabelInterval == 0
        int colLabelInterval = (int)(cols / 15);
        
        // foreach feature
        for (int f = 0; f < fts.length; f++)
        {
            // figure out the top and bottom row of the feature and the corresponding positions
            int yTopTick = (int)Math.round(plotRect.getY() + f * plotRect.getHeight() / fts.length);
            int yBottomTick = (int)Math.round(plotRect.getY() + (f+1) * plotRect.getHeight() / fts.length);
            // great, and the label will be centered between
            
            // draw the tick marks (bottom only for the last feature)
            g2.drawLine((int)Math.round(plotRect.getX() - tickLength), yTopTick, 
                (int)Math.round(plotRect.getX()), yTopTick);
            if (f == fts.length-1)
                g2.drawLine((int)Math.round(plotRect.getX() - tickLength), yBottomTick, 
                    (int)Math.round(plotRect.getX()), yBottomTick);
            
            // and the tick label
            
            // compute the size of this label
            String label = fts[f];
            Rectangle2D labelRect =(g2.getFontMetrics(fTickLabel)).getStringBounds(label, g2);

            // lower-left of label is a szSmallBuf left of the plot and centered between its tickmarks
            int xLabelPos = (int)Math.round(plotRect.getX() - szSmallBuf);
            int yLabelPos = (int)Math.round(labelRect.getWidth() / 2 + (yTopTick + yBottomTick) / 2);

            g2.rotate(-java.lang.Math.PI/2, xLabelPos, yLabelPos);
            g2.drawString(label, xLabelPos, yLabelPos);
            g2.rotate(java.lang.Math.PI/2, xLabelPos, yLabelPos);
            
        }
        
        // this is the same as in AbstractSimGraph
        for (int col = 0; col < cols; col++)
        {
            // position of the left of each column
            int xPos = (int)Math.round(plotRect.getX() + (col * boxWidth));
            
            // draw the tick mark
            g2.drawLine(xPos, (int)Math.round(plotRect.getY() + plotRect.getHeight()),
                    xPos, (int)Math.round(plotRect.getY() + plotRect.getHeight() + tickLength));
            
            if (col % colLabelInterval == 0)
            {
                // compute the size of this label
                String label = Integer.toString(col);
                Rectangle2D labelRect =(g2.getFontMetrics(fTickLabel)).getStringBounds(label, g2);
                
                // lower-left of label is a szSmallBuf + szTickLabel below the plot and centered on its tickmark
                int xLabelPos = (int)Math.round(xPos - labelRect.getWidth() / 2);
                int yLabelPos = (int)Math.round(plotRect.getY() + plotRect.getHeight() + szSmallBuf + szXTickLabel);
                
                g2.drawString(Integer.toString(col), xLabelPos, yLabelPos);
                
            }
        }
    }
    
    
}
