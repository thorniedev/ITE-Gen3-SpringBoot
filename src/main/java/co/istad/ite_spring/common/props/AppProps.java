package co.istad.ite_spring.common.props;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
@NoArgsConstructor
public class AppProps
{
    private String info;
    private String version;
    private Integer port;
}
