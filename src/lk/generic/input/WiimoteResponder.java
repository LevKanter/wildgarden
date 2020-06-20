package lk.generic.input;

/*--------------------------------------------------------------------------
	A class that is equipped to handle Wiimote button press events
	
------------------------------------------------------------------------- */
public interface WiimoteResponder 
{
	/**
	 * bang == 1 on press, bang == 0 on release
	 *  
	 */
	
	public void buttonHome(int bang);
	
	public void buttonA(int bang);
	
	public void buttonB(int bang);
	
	public void button1(int bang);
	
	public void button2(int bang);
	
	public void buttonMinus(int bang);
	
	public void buttonPlus(int bang);
	
	public void buttonUp(int bang);
	
	public void buttonDown(int bang);
	
	public void buttonLeft(int bang);
	
	public void buttonRight(int bang);
	
}
