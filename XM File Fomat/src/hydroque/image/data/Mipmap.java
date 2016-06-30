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
 * Container for an array of data which represents a mipmap. Kind of extends Image class.
 * 
 */
public class Mipmap {

	private final boolean transparency;
	
	private final Image[] data;
	
	public Mipmap(boolean transparency, Image[] data) {
		this.transparency = true;
		this.data = data;
	}
	
	/*
	 * @return whether or not at least one of the getImages()[i].hasTransparency()
	 */
	public boolean hasTransparency() {
		return transparency;
	}
	
	/*
	 * @return the number of mipmap levels. Same as getImages().length
	 */
	public int getLevels() {
		return data.length;
	}
	
	/*
	 * @return a list of images which are to be used as the mipmaps
	 */
	public Image[] getImages() {
		return data;
	}
	
}
