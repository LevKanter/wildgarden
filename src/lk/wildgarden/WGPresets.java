package lk.wildgarden;

import lk.generic.colors.ColorPresets;

/*--------------------------------------------------------------------------
	Holds default values

------------------------------------------------------------------------- */
public interface WGPresets
{			
	public static final int DEFAULT_BG = 15;
	public static final int MODAL_OVERLAY_ALPHA = 120;
	
	public static final int BUTTON_W = 268;
	public static final int BUTTON_H = 54;
	
	///////////////////////////////////////////////////////////////////////////
	// Cells
	public static final int DEFAULT_LIFESPAN = 80000;
	public static final int DEFAULT_FILL_COLOR = ColorPresets.WHITE;
	public static final int DEFAULT_CELL_SIZE = 4;
	
	///////////////////////////////////////////////////////////////////////////
	// Force fields
	public static final int DEFAULT_DENSITY_X = 30;
	public static final int DEFAULT_DENSITY_Y = 30;
	public static final float DEFAULT_DRAW_MAG_SCALAR = 9f;
	public static final float DEFAULT_DRAW_ANGLE_SCALAR = 0.2f;
	
}
