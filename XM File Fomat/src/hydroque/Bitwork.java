/*
 * Copyright (C) 2016  Polite Kiwi Programs
 * 
 * This file is part of a free library: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation, either version 3 of the
 * License.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * See the GNU General Public License for more details.
 * <http://www.gnu.org/licenses/>
 */
package hydroque;

/*
 * Basic utility functions that are used throughout this library
 * 
 */
public class Bitwork {

	/* 
	 * Compacts 4 bytes into an int
	 * 
	 * @return the result
	 */
	public static int byteToInt(byte a, byte b, byte c, byte d) {
		return ((a&0xFF) << 24 | (b&0xFF) << 16 | (c&0xFF) << 8 | (d&0xFF));
	}
	
	/*
	 * Overload method for {@link byteToInt}
	 * 
	 * @param a the list of bytes to be converted
	 * 
	 * @return the result of {@link byteToInt}
	 */
	public static int byteToInt(byte[] a) {
		return byteToInt(a[3], a[2], a[1], a[0]);
	}
	
	/*
	 * Creates a new byte array with the 4 byte segments of an int
	 * 
	 * @return the result
	 */
	public static byte[] intToByte(int a) {
		return new byte[] {
			(byte) (a >>> 0),
			(byte) (a >>> 8),
			(byte) (a >>> 16),
			(byte) (a >>> 24)
		};
	}
	
	/*
	 * Creates a new byte array with the 2 byte segments of an int
	 * 
	 * @return the result
	 */
	public static byte[] intShortToByte(int a) {
		return new byte[] {
			(byte) (a >>> 0),
			(byte) (a >>> 8),
			(byte) (a >>> 16),
			(byte) (a >>> 24)
		};
	}
	
	/*
	 * Converts a byte array to an int array. It is manditory that: a.length % 4 == 0
	 * 
	 * @param the list of bytes
	 * 
	 * @return a int array, where each int is 4 bytes of the array. See {@link byteToInt}
	 */
	public static int[] byteRangeToInt(byte[] a) {
		final int[] out = new int[a.length/4];
		int pointer = 0;
		for (int i=0; i<a.length; i+=4)
			out[pointer++] = byteToInt(a[i+3], a[i+2], a[i+1], a[i]);
		return out;
	}
	
}
