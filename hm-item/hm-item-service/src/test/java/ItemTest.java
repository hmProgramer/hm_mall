import com.hm.common.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class ItemTest {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("phone","185");
        map.put("code","123");
        String remove = map.remove("phone");
        String s = JsonUtils.toString(map);
        System.out.println(remove);
        System.out.println(s);

    }
}
