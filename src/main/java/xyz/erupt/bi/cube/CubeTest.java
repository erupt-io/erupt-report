package xyz.erupt.bi.cube;

import lombok.Getter;
import lombok.Setter;
import xyz.erupt.bi.cube.annotation.*;
import xyz.erupt.bi.cube.constant.SqlType;

@Getter
@Setter
@Cube(
        title = "测试",
        sql = """
                      select * from t_user
                      where 1=1
                      #if($a)
                        and name like concat('%', :a, '%')
                      #end
                      #if($b)
                        and gender = :gender
                      #end
                      #if($ids && $ids.size() > 0)
                        and id in (:ids)
                      #end
                      ;
                """,
        explores = {
                @Explore(
                        name = "AA", where = "name = '1'", cacheTime = 100, params = @Explore.Param(name = "a", value = "100"),
                        joins = @Join(cube = CubeJoinTest.class, type = "inner join", sqlOn = "CubeTest.name = CubeJoinTest.name")
                )
        },
        sqlType = SqlType.SUB_QUERY,
        parameters = {
                @Parameter(name = "a"),
                @Parameter(name = "b"),
                @Parameter(name = "ids", required = true)
        }
)
public class CubeTest {

    @Dimension(
            title = "姓名",
            tags = {"a", "b"}
    )
    private String name;

    @Dimension(
            title = "年龄",
            sql = """
                      case
                          when age = 0 then 'On-time'
                          when age > 0 then 'Late'
                          when age < 0 then 'Early'
                          else $a
                      end
                    """,
            format = "'---->' + value"
    )
    private Integer age;

    @Dimension(
            title = "性别"
    )
    private String sex;

    @Measure(
            title = "数量", sql = "count(1)"
    )
    private Integer count;

    @Measure(
            title = "金额", sql = "sum(money)"
    )
    private Double sum;

}
