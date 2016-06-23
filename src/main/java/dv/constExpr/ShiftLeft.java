package dv.constExpr;

import java.math.BigInteger;

import dv.Util;

public class ShiftLeft extends BinaryExpr {

	public ShiftLeft(Expression leftOperand, Expression rightOperand) {
		super("<<", leftOperand, rightOperand);
	}

	@Override
	public Object evaluate() throws EvaluationException {
		 try
		    {
		      Number l = (Number)left ().evaluate ();
		      Number r = (Number)right ().evaluate ();
		      
		      if (l instanceof Float || l instanceof Double || r instanceof Float || r instanceof Double)
		      {
		        String[] parameters = {Util.getMessage ("EvaluationException.left"), left ().value ().getClass ().getName (), right ().value ().getClass ().getName ()};
		        throw new EvaluationException (Util.getMessage ("EvaluationException.1", parameters));
		      }
		      else
		      {
		        BigInteger bL = (BigInteger)l;
		        BigInteger bR = (BigInteger)r;

		        BigInteger ls  = bL.shiftLeft (bR.intValue ());

		        value (ls);
		      }
		    }
		    catch (ClassCastException e)
		    {
		      String[] parameters = {Util.getMessage ("EvaluationException.left"), left ().value ().getClass ().getName (), right ().value ().getClass ().getName ()};
		      throw new EvaluationException (Util.getMessage ("EvaluationException.1", parameters));
		    }
		    return value ();
	}

}
