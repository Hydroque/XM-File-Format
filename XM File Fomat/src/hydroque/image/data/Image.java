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
package hydroque.image.data;

/*
 * data container for an abstract image
 * 
 */
public class Image {
	
	// Color Models
	public static final int MONOCHROME = 1, RGB = 3, RGBA = 4;
	
	// Compression Type
	public static final int NONE = 0, INFLATE = 1;
	
	private final int width, height;
	private final int color_model;
	private final int compression;
	
	private final byte[] body;
	
	public Image(int width, int height, int color_model, int compression, byte[] body) {
		this.width = width;
		this.height = height;
		this.color_model = color_model;
		this.compression = compression;
		this.body = body;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getColorModel() {
		return color_model;
	}
	
	public int getCompression() {
		return compression;
	}
	
	public byte[] getBody() {
		return body;
	}
	
}
