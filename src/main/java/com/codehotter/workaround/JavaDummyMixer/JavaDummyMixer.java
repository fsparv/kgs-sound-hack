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

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public final class JavaDummyMixer implements Mixer {
    private final List<JavaDummyMixerLine> sourceLines = new ArrayList<JavaDummyMixerLine>();
    private final List<JavaDummyMixerLine> targetLines = new ArrayList<JavaDummyMixerLine>();
    private static Mixer _realMixer = null;
    private static JavaDummyMixer _instance = null;

    synchronized public static JavaDummyMixer getInstance() {
        if (_instance == null) {
            _instance = new JavaDummyMixer();
        }
        if (_instance._realMixer == null) {
            return null;
        } 
        return _instance;
    }

    private JavaDummyMixer() {
        try {
            Mixer.Info ainfo[] = AudioSystem.getMixerInfo();
            for(int i = 0; i < ainfo.length; i++) {
                if(ainfo[i].getName() != JavaDummyMixerInfo.getInfo().getName()) {
                    _realMixer = AudioSystem.getMixer(ainfo[i]);
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
        }
    }

   @Override
    public Line getLine(Line.Info info) throws LineUnavailableException {
        Line realLine = _realMixer.getLine(info);
        return new JavaDummyMixerLine(realLine);
    }

    @Override
    public int getMaxLines(Line.Info info) {
        return _realMixer.getMaxLines(info);
    }

    @Override
    public Info getMixerInfo() {
        return JavaDummyMixerInfo.getInfo();
    }

    public Line.Info[] getSourceLineInfo() {
        return _realMixer.getSourceLineInfo();
    }

    @Override
    public Line.Info[] getSourceLineInfo(Line.Info info) {
        return _realMixer.getSourceLineInfo(info);
    }

    @Override
    public Line[] getSourceLines() {
        return sourceLines.toArray(new Line[0]);
    }

    @Override
    public Line.Info[] getTargetLineInfo() {
        return _realMixer.getTargetLineInfo();
    }

    @Override
    public Line.Info[] getTargetLineInfo(Line.Info info) {
        return _realMixer.getTargetLineInfo(info);
    }

    @Override
    public Line[] getTargetLines() {
        return (Line[]) targetLines.toArray(new Line[0]);
    }

    @Override
    public boolean isLineSupported(Line.Info info) {
        return _realMixer.isLineSupported(info);
    }

    @Override
    public boolean isSynchronizationSupported(Line[] lines, boolean maintainSync) {
        List<Line> realLines = new ArrayList<Line>();
        for ( JavaDummyMixerLine line : (JavaDummyMixerLine[]) lines)
            realLines.add(line.realLine);
        return isSynchronizationSupported((Line[])realLines.toArray(new Line[0]), maintainSync);
    }

    @Override
    public void synchronize(Line[] lines, boolean maintainSync) {
        try {        
            List<Line> realLines = new ArrayList<Line>();
            for ( JavaDummyMixerLine line : (JavaDummyMixerLine[]) lines)
                realLines.add(line.realLine);
            synchronize((Line[])realLines.toArray(new Line[0]), maintainSync);
        } catch (IllegalArgumentException e) {
        }
    }

    @Override
    public void unsynchronize(Line[] lines) {
        try {
            List<Line> realLines = new ArrayList<Line>();
            for ( JavaDummyMixerLine line : (JavaDummyMixerLine[]) lines)
                realLines.add(line.realLine);
            unsynchronize((Line[])realLines.toArray(new Line[0]));
        } catch (IllegalArgumentException e) {
        }
    }

    @Override
    public void addLineListener(LineListener listener) {
        _realMixer.addLineListener(listener);
    }

    @Override
    synchronized public void close() {
        _realMixer.close();
    }

    @Override
    public Control getControl(Control.Type control) {
        try {
            return _realMixer.getControl(control);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Control[] getControls() {
        return _realMixer.getControls();
    }

    @Override
    public javax.sound.sampled.Line.Info getLineInfo() {
        return _realMixer.getLineInfo();
    }

    @Override
    public boolean isControlSupported(Control.Type control) {
        return _realMixer.isControlSupported(control);
    }

    @Override
    public boolean isOpen() {
        return _realMixer.isOpen();
    }

    @Override
    public void open() throws LineUnavailableException {
        _realMixer.open();
    }

    @Override
    public void removeLineListener(LineListener listener) {
        _realMixer.removeLineListener(listener);
    }

    void addLine(JavaDummyMixerLine line) {
        if ((line.getLineInfo().getLineClass() == SourceDataLine.class)) {
            sourceLines.add(line);
        }
        if ((line.getLineInfo().getLineClass() == TargetDataLine.class)) {
            targetLines.add(line);
        }
    }

    void removeLine(JavaDummyMixerLine line) {
        if ((line.getLineInfo().getLineClass() == SourceDataLine.class)) {
            sourceLines.remove(line);
        }
        if ((line.getLineInfo().getLineClass() == TargetDataLine.class)) {
            targetLines.remove(line);
        }
    }
}
