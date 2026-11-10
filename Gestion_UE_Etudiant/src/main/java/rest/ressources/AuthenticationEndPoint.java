
package rest.ressources;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import entities.Credentials;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import utilities.JwtKeyProvider;

@Path("authentication")
public class AuthenticationEndPoint {

    @Context
    private UriInfo uriInfo;

    // Solution 1 : x-www-form-urlencoded
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticateUser(
            @FormParam("username") String username,
            @FormParam("password") String password) {

        return doAuthenticate(username, password);
    }

    // Solution 2 : JSON
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticateUserJson(Credentials cred) {

        if (cred == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return doAuthenticate(cred.getUsername(), cred.getPassword());
    }

    private Response doAuthenticate(String username, String password) {
        try {
            authenticate(username, password);

            String token = issueToken(username);

            return Response.ok(token).build();

        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();

        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    private void authenticate(String username, String password) {

        // Utilisateur de démonstration
        if (username == null || password == null
                || !(username.equals("ines") && password.equals("1234"))) {

            throw new SecurityException("Identifiants invalides");
        }

        System.out.println("Authenticating user... OK : " + username);
    }

    private String issueToken(String username) {

        Key key = JwtKeyProvider.getKey();

        Date expiration = toDate(
                LocalDateTime.now().plusMinutes(15L)
        );

        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        System.out.println("Token généré pour " + username
                + ", expire le : " + expiration);

        return jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {

        return Date.from(
                localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        );
    }
}
