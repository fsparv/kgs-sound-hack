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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Control;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import java.io.IOException;

public class JavaDummyMixerLine implements Clip, SourceDataLine, TargetDataLine {
    public Line realLine = null;

    public JavaDummyMixerLine (Line line) {
        realLine = line;
    }

    @Override
    public void addLineListener(LineListener listener) {
        realLine.addLineListener(listener);
    }

    @Override
    public Line.Info getLineInfo() { 
	return realLine.getLineInfo();
    }

    @Override
    public void loop(int count) {
        ((Clip)realLine).loop(count);
    }

    @Override
    public void setLoopPoints(int start, int end) {
        ((Clip)realLine).setLoopPoints(start, end);
    }

    @Override
    public void setMicrosecondPosition(long microseconds) {
        ((Clip)realLine).setMicrosecondPosition(microseconds);
    }

    @Override
    public void setFramePosition(int frames) {
        ((Clip)realLine).setFramePosition(frames);
    }

    @Override
    public long getMicrosecondLength() {
        return ((Clip)realLine).getMicrosecondLength();
    }

    @Override
    public int getFrameLength() {
        return ((Clip)realLine).getFrameLength();
    }

    @Override
    public void open(AudioInputStream stream) throws LineUnavailableException, IOException {
        ((Clip)realLine).open(stream);
    }

    @Override
    public void open(AudioFormat format, byte[] data, int offset, int bufferSize) throws LineUnavailableException {
        ((Clip)realLine).open(format, data, offset, bufferSize);
    }

    @Override
    public void open(AudioFormat format) throws LineUnavailableException {
         JavaDummyMixer parent = JavaDummyMixer.getInstance();
         parent.addLine(this);
         ((SourceDataLine)realLine).open(format);
    }

    @Override
    public void open (AudioFormat format, int bufferSize) throws LineUnavailableException {
         JavaDummyMixer parent = JavaDummyMixer.getInstance();
         parent.addLine(this);
         ((SourceDataLine)realLine).open(format, bufferSize);
    }

    @Override
    public int write (byte[] b, int off, int len) {
        return ((SourceDataLine)realLine).write(b, off, len);
    }

    @Override
    public int read (byte[] b, int off, int len) {
        return ((TargetDataLine)realLine).read(b, off, len);
    }

    @Override
    public int available() {
        return ((DataLine)realLine).available();
    }

    @Override
    public void drain() {
        ((DataLine)realLine).drain();
    }

    @Override
    public void flush() {
        ((DataLine)realLine).flush();
    }

    @Override
    public int getBufferSize() {
        return ((DataLine)realLine).getBufferSize();
    }

    @Override
    public AudioFormat getFormat() {
        return ((DataLine)realLine).getFormat();
    }

    @Override
    public int getFramePosition() {
        return ((DataLine)realLine).getFramePosition();
    }
    
    @Override
    public float getLevel() {
        return ((DataLine)realLine).getLevel();
    }
    
    @Override
    public long getLongFramePosition() {
        return ((DataLine)realLine).getLongFramePosition();
    }
    
    @Override
    public long getMicrosecondPosition() {
        return ((DataLine)realLine).getMicrosecondPosition();
    }
    
    @Override
    public boolean isActive() {
        return ((DataLine)realLine).isActive();
    }
    
    @Override
    public boolean isRunning() {
        return ((DataLine)realLine).isRunning();
    }
    
    @Override
    public void start() {
        ((DataLine)realLine).start();
    }
    
    @Override
    public void stop() {
        ((DataLine)realLine).stop();
    }

    @Override
    public void open() throws LineUnavailableException {
        realLine.open();
    }

    
    @Override
    public void close() {
        JavaDummyMixer parent = JavaDummyMixer.getInstance();
        parent.addLine(this);
        realLine.close();
    }

    @Override
    public Control getControl(Control.Type control) {
        try {
            return realLine.getControl(control);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Control[] getControls() {
        return realLine.getControls();
    }

    @Override
    public boolean isControlSupported(Control.Type control) {
        return realLine.isControlSupported(control);
    }

    @Override
    public boolean isOpen() {
        return realLine.isOpen();
    }

    @Override
    public void removeLineListener(LineListener listener) {
        realLine.removeLineListener(listener);
    }

}
