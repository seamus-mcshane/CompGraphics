/*

*/

package fractals;

import renderer.scene.*;
import renderer.scene.primitives.*;

/**
   Create a wireframe model of a Koch Curve.
   <p>
   See <a href="https://en.wikipedia.org/wiki/Koch_snowflake" target="_top">
                https://en.wikipedia.org/wiki/Koch_snowflake</a>
*/
public class KochCurve extends Model
{
   /**
      Create a Koch curve in the xy-plane with {@code n}
      subdivisions and beginning with the line segment from
      {@code (-1.0, 0.0, 0.0)} to {@code (0.1, 0.0, 0.0)}.

      @param n  number of subdivisions in this Koch Curve
   */
   public KochCurve(final int n)
   {
      this(new Vertex(-1.0, 0.0, 0.0),
           new Vertex( 1.0, 0.0, 0.0),
           n);
   }


   /**
      Create a Koch curve with {@code n} subdivisions and
      beginning with the line segment from {@link Vertex}
      {@code v0} to {@link Vertex} {@code v1}.
      <p>
      The vertices {@code v0} and {@code v1} should be in the same z-plane.

      @param v0  left {@link Vertex} of the base
      @param v1  right {@link Vertex} of the base
      @param n   number of subdivisions in this Koch Curve
   */
   public KochCurve(final Vertex v0, final Vertex v1,
                     final int n)
   {
      name = "Koch Curve (n = " + n + ")";

      addVertex(v0, v1);

      curve(0, 1, n);
   }


   /**
      If {@code n > 0}, recursively subdivide the line segment whose
      vertices are indexed by {@code vIndex0} and {@code vIndex1}.
      <p>
      If {@code n == 0}, draw the line segment whose vertices are
      indexed by {@code vIndex0} and {@code vIndex1}.
      <p>
      Given two points, {@code p0 = (x0, y0)} and {@code p1 = (x1, y1)},
      compute the new points {@code p2 = (x2, y2)}, {@code p3 = (x3, y3)},
      and {@code p4 = (x4, y4)} that determine an equilateral triangle with
      length equal to one third of the length of the given line segment.
      <pre>{@code
                               p4
                               +
                              / \
                             /   \
                            /     \
                           /       \
                          /         \
             +-----------+-----+-----+-----------+
            p0          p2           p3          p1
      }</pre>
      <p>
      The points {@code p2} and {@code p3} divide the given line
      segment into thirds. So
      <pre>{@code
         p2 = (2/3) * p0 + (1/3) * p1
         p3 = (1/3) * p0 + (2/3) * p1
      }</pre>
      <p>
      So
      <pre>{@code
         p2 = (x2, y2) = ( (2*x0 + x1)/3, (2*y0 + y1)/3 )
         p3 = (x3, y3) = ( (x0 + 2*x1)/3, (y0 + 2*y1)/3 )
      }</pre>

      <p>
      Let {@code length} equal the length of the given line segment.
      <p>
      Let {@code v0} be the vector from {@code p0} to {@code p1}.
      <pre>{@code
         v0 = (x1 - x0, y1 - y0)
      }</pre>
      Let {@code v1} be the unit vector perpendicular to {@code v0}.
      <pre>{@code
         v1 = ( (y0 - y1)/length, (x1 - x0)/length )
      }</pre>
      <p>
      Let {@code p5} be the midpoint of the line segment.
      <pre>{@code
         p5 = (x5, y5) = ( (x0+x1)/2, (y0+y1)/2 )
      }</pre>
      <p>
      The point {@code p4} lies the distance {@code sqrt(5)*length/6}
      from {@code p5} in the direction of the unit vector {@code v1}
      (this length comes from using the Pythagorean theorem).
      <pre>{@code
         p4 = p5 + sqrt(5)*length/6 * v1
            = ( (x0+x1)/2, (y0+y1)/2 ) + sqrt(5)*length/6 * ( (y0-y1)/length, (x1-x0)/length )
            = ( (x0+x1)/2, (y0+y1)/2 ) + sqrt(5)/6 * ( (y0-y1), (x1-x0) )
            = ( (x0+x1)/2 + sqrt(5)*(y0-y1)/6, (y0+y1)/2 + sqrt(5)*(x1-x0)/6 )
      }</pre>

      @param vIndex0  index of the left {@link Vertex} of the base
      @param vIndex1  index of the right {@link Vertex} of the base
      @param n        number of subdivisions in this C Curve
   */
   private void curve(final int vIndex0, final int vIndex1,
                      final int n)
   {
      if (n > 0)
      {
         final Vertex v0 = vertexList.get(vIndex0);
         final Vertex v1 = vertexList.get(vIndex1);
         final int index = vertexList.size();

         final double x0 = v0.x;
         final double y0 = v0.y;
         final double z0 = v0.z;
         final double x1 = v1.x;
         final double y1 = v1.y;
         final double z1 = v1.z; // should have z1 == z0

         // Add three vertices to the model.
         final double sqrt5 = Math.sqrt(5);
         addVertex(new Vertex( (2*x0 + x1)/3,
                               (2*y0 + y1)/3,
                                z0),
                   new Vertex( (x0 + 2*x1)/3,
                               (y0 + 2*y1)/3,
                                z0),
                   new Vertex( (x0+x1)/2 + sqrt5*(y0-y1)/6,
                               (y0+y1)/2 + sqrt5*(x1-x0)/6,
                                z0) );

         // Give a name to the index of each of the three new vertices.
         final int vIndex2 = index + 0;
         final int vIndex3 = index + 1;
         final int vIndex4 = index + 2;

         // Recursively subdivide the four new intervals.
         curve(vIndex0, vIndex2, n-1);
         curve(vIndex2, vIndex4, n-1);
         curve(vIndex4, vIndex3, n-1);
         curve(vIndex3, vIndex1, n-1);
      }
      else
      {
         addPrimitive(new LineSegment(vIndex0,  vIndex1));
      }
   }
}//KochCurve
