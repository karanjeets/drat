package drat.proteus.rest;

import com.google.gson.Gson;
import drat.proteus.services.general.Item;
import drat.proteus.services.general.ServiceStatus;
import drat.proteus.services.health.HealthMonitorService;
import drat.proteus.services.product.ProductService;
import drat.proteus.services.rat.RatAggregateJobCountItem;
import drat.proteus.services.repo.Repository;
import org.wicketstuff.rest.annotations.MethodMapping;
import org.wicketstuff.rest.annotations.parameters.RequestParam;
import org.wicketstuff.rest.resource.gson.GsonRestResource;
import org.wicketstuff.rest.utils.http.HttpMethod;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by stevenfrancus on 10/30/15.
 */
public class ServicesRestResource extends GsonRestResource {
    private ProductService productService;
    private HealthMonitorService healthMonitorService;
    public ServicesRestResource() {
        productService = new ProductService();
        healthMonitorService = new HealthMonitorService();
    }
    @MethodMapping(value = "/products", httpMethod = HttpMethod.GET)
    public List<Item> getRecentProducts() {
        return productService.getAllRecentProducts();
    }

    @MethodMapping(value = "/repo/breakdown/{type}", httpMethod = HttpMethod.GET)
    public List<Item> getRepoTypeBreakdown(String type) {
        switch(type.toUpperCase()) {
            case "LICENSE": {
                return this.getLicenseTypeBreakdown();
            }
            case "MIME": {
                return this.getMimeTypeBreakdown();
            }
            default: {
                return null;
            }
        }
    }

    @MethodMapping(value = "/repo/size", httpMethod = HttpMethod.GET)
    public Repository.Size getRepositorySize(@RequestParam("dir") String dirPath) {
        return new Repository(dirPath).getSize();
    }

    private List<Item> getLicenseTypeBreakdown() {
        return new ArrayList<>();
    }
    private List<Item> getMimeTypeBreakdown() {
        return new ArrayList<>();
    }

    @MethodMapping(value = "/status/{type}", httpMethod = HttpMethod.GET)
    public ServiceStatus getRunningStatus(String type) {
        switch(type.toUpperCase()) {
            case "DRAT": {
                return healthMonitorService.getDratStatus();
            }
            case "OODT": {
                return healthMonitorService.getOodtStatus();
            }
            default: {
                return null;
            }
        }
    }

    @MethodMapping(value = "/status/oodt/raw", httpMethod = HttpMethod.GET)
    public Object getOodtRawHealthStatus() {
        String jsonBody = healthMonitorService.rerouteHealthMonitorData().readEntity(String.class);
        return (Map<String,Object>)new Gson().fromJson(jsonBody, Map.class);
    }
}
