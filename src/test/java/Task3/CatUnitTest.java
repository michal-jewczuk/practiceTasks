package Task3;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CatUnitTest {

    static final String CAT_NAME1 = "Baron";
    static final String CAT_NAME2 = "Bonifacy";
    static final String CAT_NAME3 = "Mruczek";
    static final String CAT_NAME4 = "Luna";
    static final String CAT_NAME5 = "AnotherCat";
    static final String CAT_NAME6 = "OtherCat";

    private Cat cat1, cat2, cat3, cat4, cat5;

    @BeforeEach
    void init() {
        cat1 = new Cat(9, CAT_NAME3);
        cat2 = new Cat(4, CAT_NAME1);
        cat3 = new Cat(2, CAT_NAME2);
        cat4 = new Cat(5, CAT_NAME1);
        cat5 = new Cat(5, CAT_NAME4);
    }

    @Nested
    @DisplayName("equality of objects tested using Set of objects")
    class SetCases {
        private Set<Cat> cats;

        @BeforeEach
        void prepare() {
            cats = new HashSet<>();
            cats.add(cat1);
            cats.add(cat2);
            cats.add(cat3);
            cats.add(cat5);
        }

        @Nested
        @DisplayName("should")
        class HappyCases {

            @ParameterizedTest
            @MethodSource("Task3.CatUnitTest#generateCatsWithNewNames")
            @DisplayName("add a cat to the set when name is not yet present in this set")
            void whenAddingACatWithNameNotYetPresentInSet(Cat cat) {
                //given
                int originalSize = cats.size();

                //when
                cats.add(cat);

                //then
                assertEquals(originalSize + 1, cats.size());
            }

            @ParameterizedTest
            @MethodSource("Task3.CatUnitTest#generateCatsWithOldNames")
            @DisplayName("remove a cat from the set when name is already present in this set")
            void whenRemovingACatWithNameAlreadyPresentInSet(Cat cat) {
                //given
                int originalSize = cats.size();

                //when
                cats.remove(cat);

                //then
                assertEquals(originalSize - 1, cats.size());
            }

        }

        @Nested
        @DisplayName("should not")
        class AlternativeCases {

            @ParameterizedTest
            @MethodSource("Task3.CatUnitTest#generateCatsWithOldNames")
            @DisplayName("add a cat to the set when name is already in this set")
            void whenAddingACatWithNameAlreadyPresentInSet(Cat cat) {
                //given
                int originalSize = cats.size();

                //when
                cats.add(cat);

                //then
                assertEquals(originalSize, cats.size());
            }

            @ParameterizedTest
            @MethodSource("Task3.CatUnitTest#generateCatsWithNewNames")
            @DisplayName("remove a cat from the set when name is not present in this set")
            void whenRemovingACatWithNameNotPresentInSet(Cat cat) {
                //given
                int originalSize = cats.size();

                //when
                cats.remove(cat);

                //then
                assertEquals(originalSize, cats.size());
            }
        }

    }

    @Nested
    @DisplayName("sorting of objects tested using Comparator")
    class ComparatorCases {

        private List<Cat> cats;
        private final CatComparator catComparator = new CatComparator();

        @BeforeEach
        void prepare() {
            cats = new ArrayList<>();
            cats.add(cat1);
            cats.add(cat2);
            cats.add(cat3);
            cats.add(cat4);
            cats.add(cat5);
        }

        @Nested
        @DisplayName("should")
        class HappyCases {

            @Test
            @DisplayName("be equal to the assumed order of the list after sorting: Luna,Baron(5),Baron(4),Mruczek,Bonifacy")
            void whenSortingOriginalList() {
                //when
                Collections.sort(cats, catComparator);

                //then
                assertAll( () -> assertEquals(CAT_NAME4, cats.get(0).getName()),
                           () -> assertEquals(CAT_NAME1, cats.get(1).getName()),
                           () -> assertEquals(5, cats.get(1).getLives()),
                           () -> assertEquals(CAT_NAME1, cats.get(2).getName()),
                           () -> assertEquals(4, cats.get(2).getLives()),
                           () -> assertEquals(CAT_NAME3, cats.get(3).getName()));
            }

            @Test
            @DisplayName("put the element with null lives amount before the element with the same name but lives amount smaller than Integer.MAX_VALUE")
            void whenElementWithNullLivesAmountAndTheSameNameAdded() {
                //given
                cats.add(new Cat(null, CAT_NAME2));

                //when
                Collections.sort(cats, catComparator);

                //then
                assertAll( () -> assertEquals(CAT_NAME2, cats.get(4).getName()),
                           () -> assertEquals(Integer.MAX_VALUE, cats.get(4).getLives()),
                           () -> assertEquals(CAT_NAME2, cats.get(5).getName()),
                           () -> assertEquals(2, cats.get(5).getLives()));
            }
        }
    }

    @Nested
    @DisplayName("filtering of objects tested using Predicate")
    class PredicateCases {

        private List<Cat> cats;
        private final Predicate<Cat> namePredicate = c -> c.getName() == CAT_NAME1;
        private final Predicate<Cat> livesPredicate = c -> c.getLives() > 3 && c.getLives() < 7;

        @BeforeEach
        void prepare() {
            cats = new ArrayList<>();
            cats.add(cat1);
            cats.add(cat2);
            cats.add(cat3);
            cats.add(cat4);
            cats.add(cat5);
        }

        @Nested
        @DisplayName("should")
        class HappyCases {

            @Test
            @DisplayName("contain 3 cat objects after filtering by name OR lives Predicate")
            void whenFilteringByNameOrLivesPredicate() {
                //when
                List<Cat> filteredCats = cats.stream()
                        .filter(namePredicate.or(livesPredicate))
                        .collect(Collectors.toList());

                //then
                assertEquals(3, filteredCats.size());
                assertAll( () -> assertTrue(filteredCats.contains(cat5)),
                           () -> assertTrue(filteredCats.contains(cat4)),
                           () -> assertTrue(filteredCats.contains(cat2)),
                           () -> assertFalse(filteredCats.contains(cat1)),
                           () -> assertFalse(filteredCats.contains(cat3)));
            }

            @Test
            @DisplayName("contain 2 cat objects after filtering by name AND lives Predicate")
            void whenFilteringByNameAndLivesPredicate() {
                //when
                List<Cat> filteredCats = cats.stream()
                        .filter(namePredicate.and(livesPredicate))
                        .collect(Collectors.toList());

                //then
                assertEquals(2, filteredCats.size());
                assertAll( () -> assertTrue(filteredCats.contains(cat2)),
                           () -> assertTrue(filteredCats.contains(cat4)),
                           () -> assertFalse(filteredCats.contains(cat1)),
                           () -> assertFalse(filteredCats.contains(cat3)),
                           () -> assertFalse(filteredCats.contains(cat5)));
            }
        }

        @Nested
        @DisplayName("should not")
        class AlternativeCases {

            @Test
            @DisplayName("contain any cat objects after filtering by name AND negated lives Predicate")
            void whenFilteringByNameAndNegateLivesPredicate() {
                //when
                List<Cat> filteredCats = cats.stream()
                        .filter(namePredicate.and(livesPredicate.negate()))
                        .collect(Collectors.toList());

                //then
                assertEquals(0, filteredCats.size());
            }
        }
    }

    private static List<Cat> generateCatsWithNewNames() {
        List<Cat> newCats = new ArrayList<>();
        newCats.add(new Cat(1, CAT_NAME5));
        newCats.add(new Cat(9, CAT_NAME6));
        newCats.add(new Cat(null, CAT_NAME6));
        return newCats;
    }

    private static List<Cat> generateCatsWithOldNames() {
        List<Cat> newCats = new ArrayList<>();
        newCats.add(new Cat(1, CAT_NAME1));
        newCats.add(new Cat(9, CAT_NAME2));
        newCats.add(new Cat(null, CAT_NAME3));
        return newCats;
    }
}
