package cprasmu.util;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.tracks.H264TrackImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created with IntelliJ IDEA.
 * User: magnus
 * Date: 2012-04-23
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class H264Test {
    public static void main(String[] args) throws IOException {
        H264TrackImpl h264Track = new H264TrackImpl(new FileDataSourceImpl("data.h264"));
        //AACTrackImpl aacTrack = new AACTrackImpl(new FileInputStream("/home/sannies2/Downloads/lv.aac").getChannel());
        Movie m = new Movie();
        m.addTrack(h264Track);
        //m.addTrack(aacTrack);

        //{
            Container out = new DefaultMp4Builder().build(m);
            FileOutputStream fos = new FileOutputStream(new File("CPR_h264_output.mp4"));
            FileChannel fc = fos.getChannel();
            out.writeContainer(fc);
            fos.close();
       // }
    }
}