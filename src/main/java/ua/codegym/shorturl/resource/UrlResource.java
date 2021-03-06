package ua.codegym.shorturl.resource;

import io.dropwizard.hibernate.UnitOfWork;
import ua.codegym.shorturl.dao.UrlDao;
import ua.codegym.shorturl.model.UrlRecord;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static ua.codegym.shorturl.common.RequestPreconditions.checkNotNull;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UrlResource {

  private final UrlDao dao;

  public UrlResource(UrlDao urlDao) {
    this.dao = urlDao;
  }

  @POST
  @UnitOfWork
  @Path("api/v1/short")
  public String makeShort(String url) {
    checkNotNull(url, "URL could not be NULL.");
    UrlRecord fullUrl = new UrlRecord(getShortUrlId(), url);
    UrlRecord shortUrl = dao.saveOrUpdate(fullUrl);
    String getShortUrl = String.valueOf(shortUrl.getShortUrl());
    String varName = "shUrl";
    String jsonResponse = "{" + "\"" + varName +"\"" + ":" +"\"" + getShortUrl +"\""+ "}";
    return jsonResponse;
  }
  private String getShortUrlId(){
    StringBuffer shortUrlId = new StringBuffer();
    for (int i = 0; i < 6; i++) {
      int rand = 'a' + (int)(('z' - 'a' + 1) * Math.random());
      shortUrlId.append((char) rand);
    }
    return shortUrlId.toString();
  }

  @GET
  @UnitOfWork
  public Response get() {
    return Response.seeOther(URI.create("/html/index.html")).build();
  }

  @GET
  @UnitOfWork
  @Path("{id}")
  public Response getFull(
      @PathParam("id") String shortUrl) {
    checkNotNull(shortUrl, "RECORD_ID could not be NULL.");
    UrlRecord urlRecord = dao.getByShortUrl(shortUrl);
    if (urlRecord == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    String fullUrl = urlRecord.getFullUrl();
    return Response.seeOther(URI.create(fullUrl)).build();
  }
}
