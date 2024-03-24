import org.example.repository.interfaces.Repository;
import org.example.repository.TripDBRepository;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Tests {
    @Test
    public void test1(){
        ///TODO
        System.out.println("a test here ");
        Properties properties=new Properties();
        try {
            properties.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        Repository repository=new TripDBRepository(properties);
        System.out.println(repository.findAll());
    }
}
