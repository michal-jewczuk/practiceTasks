package Task3;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CatUnitTest {

    static final String CAT_BARON = "Baron";
    static final String CAT_BONIFACY = "Bonifacy";
    static final String CAT_MRUCZEK = "Mruczek";
    static final String CAT_LUNA = "Luna";
    static final String CAT_ANOTHER_CAT = "AnotherCat";
    static final String CAT_OTHER_CAT = "OtherCat";

    static final Cat CAT_MRUCZEK_9 = new Cat(9, CAT_MRUCZEK);
    static final Cat CAT_BARON_4 = new Cat(4, CAT_BARON);
    static final Cat CAT_BARON_5 = new Cat(5, CAT_BARON);
    static final Cat CAT_BONIFACY_2 = new Cat(2, CAT_BONIFACY);
    static final Cat CAT_LUNA_5 = new Cat(5, CAT_LUNA);

    private static Stream<Arguments> getCatsForAddition() {
        return Stream.of(
              Arguments.of(CAT_MRUCZEK, 7, 4),
              Arguments.of(CAT_MRUCZEK, null, 4),
              Arguments.of(null, -1, 5),
              Arguments.of(CAT_ANOTHER_CAT, 2, 5)
        );
    }

    private static Stream<Arguments> getCatsForRemoval() {
        return Stream.of(
                Arguments.of(CAT_OTHER_CAT, 21, 4),
                Arguments.of(null, 0, 4),
                Arguments.of(CAT_LUNA, null, 3),
                Arguments.of(CAT_LUNA, 5, 3)
        );
    }

    @Nested
    @DisplayName("equality of objects tested using Set of objects")
    class SetCases {
        private Set<Cat> cats;

        @BeforeEach
        void prepare() {
            cats = new HashSet<>();
            cats.add(CAT_MRUCZEK_9);
            cats.add(CAT_BARON_4);
            cats.add(CAT_BONIFACY_2);
            cats.add(CAT_LUNA_5);
        }

        @ParameterizedTest
        @MethodSource("Task3.CatUnitTest#getCatsForAddition")
        @DisplayName("Should correctly handle addition to a set a Cat of the following properties")
        void testAddition(String name, Integer lives, int expectedSize) {
            //given
            Cat miau = new Cat(lives, name);

            //when
            cats.add(miau);

            //then
            assertEquals(expectedSize, cats.size());
        }

        @ParameterizedTest
        @MethodSource("Task3.CatUnitTest#getCatsForRemoval")
        @DisplayName("Should correctly handle removal from a set a Cat of the following properties")
        void testRemoval(String name, Integer lives, int expectedSize) {
            //given
            Cat miau = new Cat(lives, name);

            //when
            cats.remove(miau);

            //then
            assertEquals(expectedSize, cats.size());
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
            cats.add(CAT_MRUCZEK_9);
            cats.add(CAT_BARON_4);
            cats.add(CAT_BARON_5);
            cats.add(CAT_BONIFACY_2);
            cats.add(CAT_LUNA_5);
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
                assertAll( () -> assertEquals(CAT_LUNA, cats.get(0).getName()),
                           () -> assertEquals(CAT_BARON, cats.get(1).getName()),
                           () -> assertEquals(5, cats.get(1).getLives()),
                           () -> assertEquals(CAT_BARON, cats.get(2).getName()),
                           () -> assertEquals(4, cats.get(2).getLives()),
                           () -> assertEquals(CAT_MRUCZEK, cats.get(3).getName()));
            }

            @Test
            @DisplayName("put the element with null lives amount before the element with the same name but lives amount smaller than Integer.MAX_VALUE")
            void whenElementWithNullLivesAmountAndTheSameNameAdded() {
                //given
                cats.add(new Cat(null, CAT_BONIFACY));

                //when
                Collections.sort(cats, catComparator);

                //then
                assertAll( () -> assertEquals(CAT_BONIFACY, cats.get(4).getName()),
                           () -> assertEquals(Integer.MAX_VALUE, cats.get(4).getLives()),
                           () -> assertEquals(CAT_BONIFACY, cats.get(5).getName()),
                           () -> assertEquals(2, cats.get(5).getLives()));
            }
        }
    }

    @Nested
    @DisplayName("filtering of objects tested using Predicate")
    class PredicateCases {

        private List<Cat> cats;
        private final Predicate<Cat> namePredicate = c -> c.getName() == CAT_BARON;
        private final Predicate<Cat> livesPredicate = c -> c.getLives() > 3 && c.getLives() < 7;

        @BeforeEach
        void prepare() {
            cats = new ArrayList<>();
            cats.add(CAT_MRUCZEK_9);
            cats.add(CAT_BARON_4);
            cats.add(CAT_BARON_5);
            cats.add(CAT_BONIFACY_2);
            cats.add(CAT_LUNA_5);
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
                assertAll( () -> assertTrue(filteredCats.contains(CAT_LUNA_5)),
                           () -> assertTrue(filteredCats.contains(CAT_BARON_5)),
                           () -> assertTrue(filteredCats.contains(CAT_BARON_4)),
                           () -> assertFalse(filteredCats.contains(CAT_MRUCZEK_9)),
                           () -> assertFalse(filteredCats.contains(CAT_BONIFACY_2)));
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
                assertAll( () -> assertTrue(filteredCats.contains(CAT_BARON_4)),
                           () -> assertTrue(filteredCats.contains(CAT_BARON_5)),
                           () -> assertFalse(filteredCats.contains(CAT_MRUCZEK_9)),
                           () -> assertFalse(filteredCats.contains(CAT_BONIFACY_2)),
                           () -> assertFalse(filteredCats.contains(CAT_LUNA_5)));
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

}
