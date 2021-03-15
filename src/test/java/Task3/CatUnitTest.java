package Task3;

import org.junit.jupiter.api.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CatUnitTest {

    @Nested
    @DisplayName("equality of objects tested using Set of objects")
    class SetCases {
        private Set<Cat> cats;

        @BeforeEach
        void prepare() {
            cats = new HashSet<>();
            cats.add(new Cat(9, "Mruczek"));
            cats.add(new Cat(4, "Baron"));
            cats.add(new Cat(2, "Bonifacy"));
            cats.add(new Cat(5, "Baron"));
            cats.add(new Cat(5, "Luna"));
        }

        @Nested
        @DisplayName("should return true")
        class HappyCases {

            @Test
            @DisplayName("when set contains an object with same name and different lives amount")
            void whenSameNameAndDifferentLivesAmount() {
                Cat cat = new Cat(1, "Luna");
                Assertions.assertTrue(cats.contains(cat));
            }

            @Test
            @DisplayName("when set contains an object with same name and same lives amount")
            void whenSameNameAndSameLivesAmount() {
                Cat cat = new Cat(5, "Baron");
                Assertions.assertTrue(cats.contains(cat));
            }

            @Test
            @DisplayName("when set contains an object with same name and null instead of lives amount")
            void whenSameNameAndNullAsLivesAmount() {
                Cat cat = new Cat(null, "Baron");
                Assertions.assertTrue(cats.contains(cat));
            }
        }

        @Nested
        @DisplayName("should return false")
        class AlternativeCases {

            @Test
            @DisplayName("when set contains an object with different name and same lives amount")
            void whenDifferentNameAndSameLivesAmount() {
                Cat cat = new Cat(9, "MRUCZEK");
                Assertions.assertFalse(cats.contains(cat));
            }

            @Test
            @DisplayName("when set contains an object with different name and different lives amount")
            void whenDifferentNameAndDifferentLivesAmount() {
                Cat cat = new Cat(1, "MRUCZEK");
                Assertions.assertFalse(cats.contains(cat));
            }

            @Test
            @DisplayName("when set contains an object with null as a name")
            void whenNullAsAName() {
                Cat cat = new Cat(1, null);
                Assertions.assertFalse(cats.contains(cat));
            }

            @Test
            @DisplayName("when null is given to compare with set objects")
            void whenNullInsteadOfAnObject() {
                Assertions.assertFalse(cats.contains(null));
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
            cats.add(new Cat(9, "Mruczek"));
            cats.add(new Cat(4, "Baron"));
            cats.add(new Cat(2, "Bonifacy"));
            cats.add(new Cat(5, "Baron"));
            cats.add(new Cat(5, "Luna"));
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
                assertAll( () -> assertEquals("Luna", cats.get(0).getName()),
                           () -> assertEquals("Baron", cats.get(1).getName()),
                           () -> assertEquals(5, cats.get(1).getLives()),
                           () -> assertEquals("Baron", cats.get(2).getName()),
                           () -> assertEquals(4, cats.get(2).getLives()),
                           () -> assertEquals("Mruczek", cats.get(3).getName()));
            }

            @Test
            @DisplayName("put the element with null lives amount before the element with the same name but lives amount smaller than Integer.MAX_VALUE")
            void whenElementWithNullLivesAmountAndTheSameNameAdded() {
                //given
                cats.add(new Cat(null, "Bonifacy"));

                //when
                Collections.sort(cats, catComparator);

                //then
                assertAll( () -> assertEquals("Bonifacy", cats.get(4).getName()),
                           () -> assertEquals(Integer.MAX_VALUE, cats.get(4).getLives()),
                           () -> assertEquals("Bonifacy", cats.get(5).getName()),
                           () -> assertEquals(2, cats.get(5).getLives()));
            }
        }
    }

    @Nested
    @DisplayName("filtering of objects tested using Predicate")
    class PredicateCases {

        private List<Cat> cats;
        private Cat cat1, cat2, cat3, cat4, cat5;
        private final Predicate<Cat> namePredicate = c -> c.getName() == "Baron";
        private final Predicate<Cat> livesPredicate = c -> c.getLives() > 3 && c.getLives() < 7;

        @BeforeEach
        void prepare() {
            cat1 = new Cat(9, "Mruczek");
            cat2 = new Cat(4, "Baron");
            cat3 = new Cat(2, "Bonifacy");
            cat4 = new Cat(5, "Baron");
            cat5 = new Cat(5, "Luna");
            cats = Arrays.asList(cat1, cat2, cat3, cat4, cat5);
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
}
