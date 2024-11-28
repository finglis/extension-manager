package qupath.ext.extensionmanager.core.index.model;

import java.net.URI;
import java.util.List;

/**
 * A description of an extension release hosted on GitHub.
 * <p>
 * Functions of this object may return null only if this object is not valid (see {@link #checkValidity()}).
 *
 * @param name the name of this release
 * @param mainUrl the GitHub URL where the main extension jar can be downloaded
 * @param requiredDependencyUrls SciJava Maven, Maven Central, or GitHub URLs where required dependency jars can be downloaded
 * @param optionalDependencyUrls SciJava Maven, Maven Central, or GitHub URLs where optional dependency jars can be downloaded
 * @param javadocsUrls SciJava Maven, Maven Central, or GitHub URLs where javadoc jars for the main extension
 *                     jar and for dependencies can be downloaded
 * @param versions a specification of minimum and maximum compatible versions
 */
public record Release(
        String name,
        URI mainUrl,
        List<URI> requiredDependencyUrls,
        List<URI> optionalDependencyUrls,
        List<URI> javadocsUrls,
        VersionRange versions
) {
    private static final List<String> VALID_HOSTS = List.of("github.com", "maven.scijava.org", "repo1.maven.org");

    /**
     * Create a Release.
     *
     * @param name the name of this release
     * @param mainUrl the GitHub URL where the main extension jar can be downloaded
     * @param requiredDependencyUrls SciJava Maven, Maven Central, or GitHub URLs where required dependency jars can be downloaded
     * @param optionalDependencyUrls SciJava Maven, Maven Central, or GitHub URLs where optional dependency jars can be downloaded
     * @param javadocsUrls SciJava Maven, Maven Central, or GitHub URLs where javadoc jars for the main extension
     *                     jar and for dependencies can be downloaded
     * @param versions a specification of minimum and maximum compatible versions
     * @throws IllegalStateException when the created object is not valid (see {@link #checkValidity()})
     */
    public Release(
            String name,
            URI mainUrl,
            List<URI> requiredDependencyUrls,
            List<URI> optionalDependencyUrls,
            List<URI> javadocsUrls,
            VersionRange versions
    ) {
        this.name = name;
        this.mainUrl = mainUrl;
        this.requiredDependencyUrls = requiredDependencyUrls;
        this.optionalDependencyUrls = optionalDependencyUrls;
        this.javadocsUrls = javadocsUrls;
        this.versions = versions;

        checkValidity();
    }

    @Override
    public List<URI> requiredDependencyUrls() {
        return requiredDependencyUrls == null ? List.of() : requiredDependencyUrls;
    }

    @Override
    public List<URI> optionalDependencyUrls() {
        return optionalDependencyUrls == null ? List.of() : optionalDependencyUrls;
    }

    @Override
    public List<URI> javadocsUrls() {
        return javadocsUrls == null ? List.of() : javadocsUrls;
    }

    /**
     * Check that this object is valid:
     * <ul>
     *     <li>The 'min', 'mainUrl', and 'versions' fields must be defined.</li>
     *     <li>The 'version' object must be valid (see {@link VersionRange#checkValidity()}).</li>
     *     <li>The 'mainURL' field must be a GitHub URL. All other URLs must be SciJava Maven, Maven Central, or GitHub URLs.</li>
     * </ul>
     *
     * @throws IllegalStateException when this object is not valid
     */
    public void checkValidity() {
        Utils.checkField(name, "name", "Release");
        Utils.checkField(mainUrl, "mainUrl", "Release");
        Utils.checkField(versions, "versions", "Release");

        versions.checkValidity();

        Utils.checkGithubURI(mainUrl);

        checkURIHostValidity(requiredDependencyUrls);
        checkURIHostValidity(optionalDependencyUrls);
        checkURIHostValidity(javadocsUrls);
    }

    private static void checkURIHostValidity(List<URI> uris) {
        if (uris != null) {
            for (URI uri: uris) {
                if (!VALID_HOSTS.contains(uri.getHost())) {
                    throw new IllegalStateException(String.format(
                            "The host part of %s is not among %s", uri, VALID_HOSTS
                    ));
                }
            }
        }
    }
}

