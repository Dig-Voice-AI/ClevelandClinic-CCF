package ccf.custom.cvpvxml.actionelement;

//import java.io.*;

public class SpanishTest
{
   public static void main(String[] args)
   {
	System.out.println("Testing Spanish!");

	// Basic small numbers
	//for(int i=0; i<=150; ++i) TestSpanish((double)i);

	// Fractions
	//for(int i=0; i<=100; ++i) TestSpanish(1.0 + (double)i/100.0);

	// Larger numbers, especially around transitions
	
	TestSpanish(1931.01);
	/*
	TestSpanish(0.23);
	TestSpanish(1.0);
	TestSpanish(1.99);
	TestSpanish(2.01);
	TestSpanish(2.13);
	TestSpanish(1001.0);
	TestSpanish(1999.0);
	TestSpanish(2000.0);
	TestSpanish(2001.0);
	TestSpanish(2012.0);
	TestSpanish(2013.0);
	TestSpanish(2099.0);
	TestSpanish(10.0);
	TestSpanish(100.0);
	TestSpanish(1000.0);
	TestSpanish(1010.0);
	TestSpanish(1100.0);
	TestSpanish(1110.0);
	TestSpanish(10000.0);
	TestSpanish(100000.0);
*/
	//TestSpanish(999999.99);

   }

   public static void TestSpanish(String s)
   {
	System.out.println(s + " : " + convertToSpanish(s));
   }

   public static String convertToSpanish(String input)
   {
	String result = "";

	String s = input.toUpperCase();	// So we don't have to worry about lower case.

	// Output each character individually
	boolean paused = false;
	for(char c : s.toCharArray())
	{
		if(Character.isLetterOrDigit(c))		// We can say these
		{
			result += c + " ";
			paused = false;
		} else {
			if(!paused) result += "_ ";		// Don't say these
			paused = true;
		}
	}

	return result;
   }

   public static void TestSpanish(int i)
   {
	System.out.println(i + " : " + convertToSpanish(i));
   }

   public static void TestSpanish(long n)
   {
	System.out.println(n + " : " + convertToSpanishLong(n));
   }

   public static void TestSpanish(double d)
   {
	System.out.println(d + " : " + convertToSpanishDollarsCents(d));
   }

// Note that Spanish numbering is European, and matches the UK English and French.
// 1,000,000,000 = 1 thousand million
// 1,000,000,000,000 = 1 billion
//
// American numbering is different. Beware!
// 1,000,000,000 = 1 billion
// 1,000,000,000,000 = 1 trillion
//


// Note that double type has limited precision, much less than long type.
// Warning: Amounts larger than     10,000,000,000,000 may not be precise.
// Long types are precise to 9,223,372,036,854,775,807 but lack cents.
// 
   public static String convertToSpanishDollarsCents(double amount)
   {

	// Check limits
	if(amount >  (double)Long.MAX_VALUE) return "ERROR: Amount over Long.MAX_VALUE";
	if(amount <= (double)Long.MIN_VALUE) return "ERROR: Amount under Long.MAX_VALUE";

	String s = "";
	String sign = "";

	// Check for negative
	if(amount < 0.0)
	{
		sign = "menos ";
		amount = -amount;
	}

	// Get fraction part
	long dollars = (long)amount;
	double fraction = amount - (double)dollars;

	// Round fraction to nearest penny
	int pennies = (int)(fraction*100+.5);

	if(pennies > 0)
	{
		s += "y " + Under100(pennies);
		if(pennies == 1) s += "centavo ";
		else s += "centavos ";
	}

	// Add lower part of dollar amount
	String dollar = "dólar ";
	if(amount != 1.00) dollar = "dólares ";
	s = convertToSpanishLong((long)amount) + dollar + s;

	return sign + s;
   }

   // Handles minimum value of +/- Long.MAX_VALUE, i.e. -9,223,372,036,854,775,807 to +9,223,372,036,854,775,807
   // Doesn't handle Long.MIN_VALUE = -9,223,372,036,854,775,808
   //
   // 10^6  = un millón,  or plural millones
   // 10^12 = un billón,  or plural billones
   // 10^18 = un trillón, or plural trillones 
   // 
   public static String convertToSpanishLong(long n)
   {
	String s = "";
	String sign = "";

	// Special case for zero
	if(n == 0) return Under100(0);

	if(n < 0)
	{
		n = -n;
		sign = "menos ";
	}

	if(n < 0)
	{
		return "ERROR convertToSpanishLong passed Long.MIN_VALUE = -9,223,372,036,854,775,808.";
	}

	// Add lower 6 digits
	int lower = (int)(n % 1000000L);
	s = Under1000000(lower);

	// See if there are 10^6's (millions in American English)
	n = n/1000000L;
	if(n > 0)
	{
		lower = (int)(n % 1000000);
		if(lower == 1)
		{
			s = "un millón " + s;
		}
		else if(lower > 1)
		{
			s = Under1000000(lower) + "millones " + s;
		}	
	}

	// See if there are 10^12's (trillions in American English)
	n = n/1000000L;
	if(n > 0)
	{
		lower = (int)(n % 1000000);
		if(lower == 1)
		{
			s = "un billón " + s;
		}
		else if(lower > 1)
		{
			s = Under1000000(lower) + "billones " + s;
		}		
	}

	// See if there are 10^18's (quintillion in American English)
	n = n/1000000L;
	if(n > 0)
	{
		lower = (int)(n % 1000000);
		if(lower == 1)
		{
			s = "un trillón " + s;
		}
		else if(lower > 1)
		{
			s = Under1000000(lower) + "trillones " + s;
		}		
	}

	// No bits left in long!	

	return sign + s;
   }


   public static String convertToSpanish(int i)
   {
	return convertToSpanishLong((long)i);
   }

   public static String Under1000000(int i)
   {
	String s = "";

	if(i < 0) return "ERROR Under1000000 passed negative value!";
	if(i >= 1000000) return "ERROR Under1000000 passed larger value!";

	int units	= i % 1000;
	int thousands	= i / 1000;	// Just in case we're passed bigger stuff

	if(thousands > 0)
	{
		if(thousands > 1)
		{
			s += Under1000(thousands);
		}
		s+= "mil ";
	}
	s += Under1000(units);

	return(s);
   }


   public static String Under1000(int i)
   {
	if(i < 0) return "ERROR Under1000 passed negative value!";
	if(i >= 1000) return "ERROR Under1000 passed larger value!";

	i = i%1000;	// Ignore digits beyond lower 3.

	int smalls	= i % 100;
	int hundreds	= i / 100;

	String s = "";

	switch(hundreds)
	{
	case 0: break;
	case 1: if(smalls > 0) { s+= "ciento "; } else { s+= "cien "; }  break;	// Just use "cien" for 100 even. Tricky! ;)
	case 2: s+= "doscientos "	; break;
	case 3: s+= "trescientos "	; break;
	case 4: s+= "cuatrocientos "	; break;
	case 5: s+= "quinientos "	; break;
	case 6: s+= "seiscientos "	; break;
	case 7: s+= "setecientos "	; break;
	case 8: s+= "ochocientos "	; break;
	case 9: s+= "novecientos "	; break;
	default: return "ERROR in hundreds!";
	}

	// Add small stuff
	if(smalls > 0) s += Under100(smalls);

	return s;
   }


   public static String Under100(int i)
   {
	// Really small stuff -- no pattern!! :(
	if(i < 30)
	{
		switch(i)
		{
		case  0: return "cero "		;
		case  1: return "un "		;		// gender issue?
		case  2: return "dos "		;
		case  3: return "tres "		;
		case  4: return "cuatro "	;
		case  5: return "cinco "	;
		case  6: return "seis "		;
		case  7: return "siete "	;
		case  8: return "ocho "		;
		case  9: return "nueve "	;
		case 10: return "diez "		;
		case 11: return "once "		;
		case 12: return "doce "		;
		case 13: return "trece "	;
		case 14: return "catorce "	;
		case 15: return "quince "	;
		case 16: return "dieciséis "	;
		case 17: return "diecisiete "	;
		case 18: return "dieciocho "	;
		case 19: return "diecinueve "	;
		case 20: return "veinte "	;
		case 21: return "veintiuno "	;		// gender issue?
		case 22: return "veintidós "	;
		case 23: return "veintitrés "	;
		case 24: return "veinticuatro "	;
		case 25: return "veinticinco "	;
		case 26: return "veintiséis "	;
		case 27: return "veintisiete "	;
		case 28: return "veintiocho "	;
		case 29: return "veintinueve "	;
		default: return "ERROR < 30!"	;
		}
	}


	// Medium small stuff, follows a pattern! :)
	String s = "";

	int units = i % 10;
	int tens  = i - units;

	switch(tens)
	{
	case 30: s+= "treinta "  ; break;
	case 40: s+= "cuarenta " ; break;
	case 50: s+= "cincuenta "; break;
	case 60: s+= "sesenta "  ; break;
	case 70: s+= "setenta "  ; break;
	case 80: s+= "ochenta "  ; break;
	case 90: s+= "noventa "  ; break;
	default: return " ERROR IN tens!!!";
	}

	if(units > 0) s += "y ";

	switch(units)
	{
        case 0: break;
	case 1:	s += "un "	; break;			// gender issue?
	case 2:	s += "dos "	; break;
	case 3:	s += "tres "	; break;
	case 4:	s += "cuatro "	; break;
	case 5:	s += "cinco "	; break;
	case 6:	s += "seis "	; break;
	case 7:	s += "siete "	; break;
	case 8:	s += "ocho "	; break;
	case 9:	s += "nueve "	; break;
	default: return " ERROR IN units!";
	}

	return(s);
   }

}
