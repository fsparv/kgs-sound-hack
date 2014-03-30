/* Oracle 7 no longer has the "Java Sound Audio Engine"
 * This Mixer implementation has a name starting with "Java"
 * and simple passes everything to the default audio device
 * in order to work around broken client code.
 *
 * May 2012, Emanuel Rietveld <codehotter@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */


package com.codehotter.workaround.JavaDummyMixer;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

public class JavaDummyMixerProvider extends
        javax.sound.sampled.spi.MixerProvider {

    @Override
    public Mixer getMixer(Info info) {
        if (info.equals(JavaDummyMixerInfo.getInfo())) {
            return JavaDummyMixer.getInstance();
        } else {
            throw new IllegalArgumentException("Mixer type not supported");
        }
    }

    @Override
    public Info[] getMixerInfo() {
        Mixer.Info[] m = { JavaDummyMixerInfo.getInfo() };
        return m;
    }

}
