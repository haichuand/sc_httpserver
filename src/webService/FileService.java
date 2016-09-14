package webService;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONObject;

/**
 * This service is used to handle both uploads and downloads of chat
 * attachments stored in the upload directory. Files uploaded will be deleted
 * after a certain period for security reasons.
 * 
 * @author Gary Ng
 */
@Path("/file")
public class FileService {

    private static final String ROOT_DIR = "/home/ec2-user/apache-tomcat-8.0.32/webapps/SuperCaly/";
    private static final String UPLOAD_DIR = ROOT_DIR + "upload/";

    private static final List<String> IMAGE_EXTENSIONS;
    private static final List<String> VIDEO_EXTENSIONS;

    private static final long PURGE_UPLOADS_MS = 30 * 60000L; // Minutes
    private static Timer TIMER;

    static {
        IMAGE_EXTENSIONS = Arrays.asList(new String[]{
            ".jpg", ".jpeg", ".png", ".gif"
        });

        VIDEO_EXTENSIONS = Arrays.asList(new String[]{
            ".mp4"
        });
    }

    /**
     * Used for uploading image attachments.
     * 
     * @param input Input stream used to read image attachment.
     * @param data Content disposition data to retrieve filename.
     * @return JSON response.
     */
    @POST
    @Path("/upload/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadImage(@FormDataParam("file") InputStream input,
            @FormDataParam("file") FormDataContentDisposition data) {
        startPurgeTimer();

        JSONObject response = new JSONObject();

        String filename = data.getFileName();
        String extension = "";
        if (filename.contains(".")) {
            extension = filename.substring(filename.lastIndexOf("."));

            if (!IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
                response.put("status", "INVALID_IMAGE_FORMAT");
                return response.toString();
            }
        }

        filename = UUID.randomUUID().toString() + extension;

        try {
            OutputStream output = new FileOutputStream(UPLOAD_DIR + filename);

            int length;
            byte[] buffer = new byte[1024];
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();

            response.put("status", "OK");
            response.put("filename", filename);
            response.put("type", "image/*");
        } catch (IOException e) {
            response.put("status", "FAILED");
            response.put("msg", e.getMessage());
        }

        return response.toString();
    }

    /**
     * Used for uploading video attachments.
     * 
     * @param input Input stream used to read image attachment.
     * @param data Content disposition data to retrieve filename.
     * @return JSON response.
     */
    @POST
    @Path("/upload/video")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadVideo(@FormDataParam("file") InputStream input,
            @FormDataParam("file") FormDataContentDisposition data) {
        startPurgeTimer();

        JSONObject response = new JSONObject();

        String filename = data.getFileName();
        String extension = "";
        if (filename.contains(".")) {
            extension = filename.substring(filename.lastIndexOf("."));

            if (!VIDEO_EXTENSIONS.contains(extension.toLowerCase())) {
                response.put("status", "INVALID_VIDEO_FORMAT");
                return response.toString();
            }
        }

        filename = UUID.randomUUID().toString() + extension;

        try {
            OutputStream output = new FileOutputStream(UPLOAD_DIR + filename);

            int length;
            byte[] buffer = new byte[1024];
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();

            response.put("status", "OK");
            response.put("filename", filename);
            response.put("type", "video/*");
        } catch (IOException e) {
            response.put("status", "FAILED");
            response.put("msg", e.getMessage());
        }

        return response.toString();
    }

    /**
     * Used for downloading attachments.
     * 
     * @param filename Filename of the attachment.
     * @return HTTP response.
     */
    @GET
    @Path("/download/{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getImage(@PathParam("filename") String filename) {
        startPurgeTimer();

        Response response;

        File file = new File(UPLOAD_DIR + filename);
        if (file.exists()) {
            ResponseBuilder builder = Response.ok(file, MediaType.APPLICATION_OCTET_STREAM);
            builder.header("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            response = builder.build();
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }

        return response;
    }

    /**
     * Timer used to schedule the deletion of existing uploaded files.
     */
    private static void startPurgeTimer() {
        if (TIMER != null) {
            TIMER.cancel();
            TIMER = null;
        }

        TIMER = new Timer();
        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                purgeUploads();
            }
        }, PURGE_UPLOADS_MS);
    }

    /**
     * Delete all files found in the upload directory.
     */
    private static void purgeUploads() {
        File[] files = new File(UPLOAD_DIR).listFiles();

        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
}
