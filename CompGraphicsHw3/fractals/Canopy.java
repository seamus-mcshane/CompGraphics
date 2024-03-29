/*

*/

package fractals;

import renderer.scene.*;
import renderer.scene.primitives.*;

/**
   Create a wireframe model of a fractal canopy tree.
   <p>
   See <a href="https://en.wikipedia.org/wiki/Fractal_canopy" target="_top">
                https://en.wikipedia.org/wiki/Fractal_canopy</a>
*/
public class Canopy extends Model
{
   /**
      Create a fractal canopy tree in the xy-plane
      with {@code n} branch splittings and starting
      with the line segment (the tree's "trunk") from
      {@code (0.0, -1.0, 0.0)} to {@code (0.0, -0.5, 0.0)}.

      @param angle  angle, in degrees, between the branches
      @param n      number of branch splittings in this tree
   */
   public Canopy(final double angle, final int n)
   {
      this(new Vertex(0.0, -1.0, 0.0),
           new Vertex(0.0, -0.5, 0.0),
           angle, n);
   }


   /**
      Create a fractal canopy tree in the xy-plane
      with {@code n} branch splittings and starting
      with the line segment (the tree's "trunk") from
      {@link Vertex} {@code v0} to {@link Vertex} {@code v1}.
      <p>
      The vertices {@code v0} and {@code v1} should be in the same z-plane.

      @param v0     1st {@link Vertex} of the tree's trunk
      @param v1     2nd {@link Vertex} of the tree's trunk
      @param angle  angle, in degrees, between the branches
      @param n      number of branch splittings in this tree
   */
   public Canopy(final Vertex v0, final Vertex v1,
                 final double angle, final int n)
   {
      name = "Canopy Tree (n = " + n + ")";

      addVertex(v0, v1);

      tree(0, 1, angle, n);
   }


   /**
      If {@code n > 0}, draw the line segment whose vertices
      are indexed by {@code vIndex0} and {@code vIndex1} and
      then recursively draw two canopy trees at the end of the
      line segment.
      <p>
      If {@code n == 0}, just draw the line segment whose vertices
      are indexed by {@code vIndex0} and {@code vIndex1}.
      <p>
      Given two points, {@code p0 = (x0, y0)} and {@code p1 = (x1, y1)},
      compute two new points {@code p2 = (x2, y2)} and {@code p3 = (x3, y3)}
      that determine two line segments that "branch out" from the given
      line segment. The angle between the branches should given by the
      parameter {@code angle}.
      <pre>{@code
            p2    p4   p3
             +    +    +
              \       /
               \     /
                \   /
                 \ /
                  + p1
                  |
                  |
                  |
                  |
                  |
                  |
                  |
                  + p0
      }</pre>
      <p>
      Let {@code length} denote the length of the line segment from
      point {@code v0} to point {@code v1}.
      <pre>{@code
         length = sqrt( (x1 - x0)*(x1 - x0) + (y1 - y0)*(y1 - y0) )
      }</pre>
      <p>
      Let {@code v0} be the unit vector in the direction from {@code p0}
      to {@code p1}.
      <pre>{@code
         v0 = (x1 - x0, y1 - y0) / length
      }</pre>
      <p>
      Let the point {@code p4 = (x4, y4)} be in the direction of {@code v0}
      away from the point {@code p1} at a distance of {@code length/sqrt(2)}
      <pre>{@code
         p4 = p1 + (v0 * length/sqrt(2))
            = (x1, y1) + (x1 - x0, y1 - y0)/sqrt(2)
            = (x1 + (x1 - x0)/sqrt(2), y1 + (y1 - y0)/sqrt(2))
            = ((1+sqrt(2))*x1 - x0, (1+sqrt(2))*y1 - y0)/sqrt(2)
      }</pre>
      <p>
      The distance between {@code p2} and {@code p4} is the
      same as the distance between {@code p4} and {@code p3}.
      <p>
      The points {@code p2}, {@code p4}, and {@code p1} make up a right
      triangle with the angle at {@code p1} being {@code angle/2}. Using
      this right triangle, we can see that the distance between {@code p2}
      and {@code p4} is given by
      <pre>{@code
         (length/sqrt(2) * tan(angle/2)
      }</pre>
      <p>
      Let {@code v1} be a (unit) vector perpendicular to {@code v0}.
      <pre>{@code
         v1 = (y0 - y1, x1 - x0) / length
      }</pre>
      <p>
      Notice that
      <pre>{@code
         p2 = p4 - ((length/sqrt(2) * tan(angle/2)) * v1
            = p4 - tan(angle/2)/sqrt(2) * (y0 - y1, x1 - x0)
         p3 = p4 + ((length/sqrt(2) * tan(angle/2)) * v1
              p4 + tan(angle/2)/sqrt(2) * (y0 - y1, x1 - x0)
      }</pre>
      <p>
      So
      <pre>{@code
         p2 = (x4, y4) - tan(angle/2)/sqrt(2) * (y0 - y1, x1 - x0)
            = ((1+sqrt(2))*x1 - x0, (1+sqrt(2))*y1 - y0)/sqrt(2) - tan(angle/2)/sqrt(2) * (y0 - y1, x1 - x0)
            = (((1+sqrt(2))*x1 - x0 - tan(angle/2) * (y0 - y1))/sqrt(2),
               ((1+sqrt(2))*y1 - y0 - tan(angle/2) * (x1 - x0))/sqrt(2))
      }</pre>
      <p>
      and
      <pre>{@code
         p3 = (x4, y4) + tan(angle/2)/sqrt(2) * (y0 - y1, x1 - x0)
            = ((1+sqrt(2))*x1 - x0, (1+sqrt(2))*y1 - y0)/sqrt(2) + tan(angle/2)/sqrt(2) * (y0 - y1, x1 - x0)
            = (((1+sqrt(2))*x1 - x0 + tan(angle/2) * (y0 - y1))/sqrt(2),
               ((1+sqrt(2))*y1 - y0 + tan(angle/2) * (x1 - x0))/sqrt(2))
      }</pre>

      @param vIndex0  index of the left {@link Vertex} of the base
      @param vIndex1  index of the right {@link Vertex} of the base
      @param angle    angle, in degrees, between the branches
      @param n        number of branches in this H tree
   */
   private void tree(final int vIndex0, final int vIndex1,
                     double angle, final int n)
   {
      addPrimitive(new LineSegment(vIndex0,  vIndex1));

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

         // Add two vertices to the model.
         final double sqrt2 = Math.sqrt(2);
         final double angleInRad = Math.PI/180*angle/2;
         final double tanAngle = Math.tan(angleInRad);
         addVertex(new Vertex(
                    ((1+sqrt2)*x1 - x0 - tanAngle * (y0 - y1))/sqrt2,
                    ((1+sqrt2)*y1 - y0 - tanAngle * (x1 - x0))/sqrt2,
                    z0),
                   new Vertex(
                    ((1+sqrt2)*x1 - x0 + tanAngle * (y0 - y1))/sqrt2,
                    ((1+sqrt2)*y1 - y0 + tanAngle * (x1 - x0))/sqrt2,
                    z0));

         // Give a name to the index of each of the four new vertices.
         final int vIndex2 = index + 0;
         final int vIndex3 = index + 1;

         // Recursively branch the two new intervals.
         tree(vIndex1, vIndex2, angle, n-1);
         tree(vIndex1, vIndex3, angle, n-1);
      }
   }
}//Canopy
