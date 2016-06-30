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
package hydroque.texture.data;

/*
 * data container for an abstract image
 * 
 */
public class Image {
	
	public final int width, height;
	public final boolean transparency;
	
	public final byte[] body;
	
	public Image(int width, int height, boolean transparency, byte[] body) {
		this.width = width;
		this.height = height;
		this.transparency = transparency;
		this.body = body;
	}
	
	/*
	 * @return true if each 4th pixel in getPixels() are all opaque, else transulcent and false
	 */
	public boolean hasTransparency() {
		return transparency;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	/*
	 * @return the pixel array of bytes
	 */
	public byte[] getPixels() {
		return body;
	}
	
}
