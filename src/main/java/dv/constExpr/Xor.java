package dv.constExpr;

import java.math.BigInteger;

import dv.Util;

public class Xor extends BinaryExpr {

	public Xor(Expression leftOperand, Expression rightOperand) {
		super("^", leftOperand, rightOperand);
	}

	@Override
	public Object evaluate() throws EvaluationException {
		 try
		    {
		      Number l = (Number)left ().evaluate ();
		      Number r = (Number)right ().evaluate ();

		      if (l instanceof Float || l instanceof Double || r instanceof Float || r instanceof Double)
		      {
		        String[] parameters = {Util.getMessage ("EvaluationException.xor"), left ().value ().getClass ().getName (), right ().value ().getClass ().getName ()};
		        throw new EvaluationException (Util.getMessage ("EvaluationException.1", parameters));
		      }
		      else
		      {
		        BigInteger uL = (BigInteger)l;
		        BigInteger uR = (BigInteger)r;
		        value (uL.xor (uR));
		      }
		    }
		    catch (ClassCastException e)
		    {
		      String[] parameters = {Util.getMessage ("EvaluationException.xor"), left ().value ().getClass ().getName (), right ().value ().getClass ().getName ()};
		      throw new EvaluationException (Util.getMessage ("EvaluationException.1", parameters));
		    }
		    return value ();
	}

}
