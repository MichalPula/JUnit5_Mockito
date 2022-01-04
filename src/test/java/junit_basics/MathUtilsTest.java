package junit_basics;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import pulson.junit_mockito.junit_basics.MathUtils;
import pulson.junit_mockito.junit_basics.SeasonEnum;
import pulson.junit_mockito.mockito.animal.Animal;
import org.junit.jupiter.api.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MathUtilsTest {

    private MathUtils mathUtils;

    private Integer counter = 0;

    @BeforeEach
    void beforeEachParent() {
        //System.out.println("Parent @BeforeEach");
        counter = 0;
        mathUtils = new MathUtils();
    }

    @Nested
    @DisplayName("Tests checking counter inside NestedClass")
    class NestedClass {
        @BeforeEach
        void beforeEachChild() {
            //System.out.println("Child @BeforeEach");
            counter = 10;
        }
        @Test
        void Should_return10_When_BeforeEachChildMethodWorks() {
            assertEquals(10, counter);
        }
        @Test
        void Should_return20_When_BeforeEachChildMethodWorks() {
            assertEquals(20, counter * 2);
        }
    }


    @Test
    @Order(value = 2)
    void orderedTest1() {
        assertTrue(10 > 5);
    }
    @Test
    @Order(value = 1)
    void orderedTest2() {
        assertTrue(20 > 10);
    }


    @TestFactory
    Collection<DynamicTest> dynamicTestsFromCollection() {
        return Arrays.asList(
                dynamicTest("1st dynamic test", () -> assertTrue("ok".length() == 2)),
                dynamicTest("2nd dynamic test", () -> assertEquals(4, mathUtils.add(2, 2))));
    }


    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    //Pozwala ustalić maksymalny czas trwania testu
    void failsIfExecutionTimeExceeds100Milliseconds() {
    }


    @Test
    @DisplayName(value = "Should return 7 when add(5, 2) works properly")
    void Should_Return7_When_Adding5And2() {
        //given
        int a = 5;
        int b = 2;
        //when
        int result = mathUtils.add(a, b);
        //then
        assertEquals(7, result, "The add method should add numbers");
    }


    @RepeatedTest(value = 5, name = "Repetition {currentRepetition} of {totalRepetitions}")
    void Should_PrintSentence5Times() {
        System.out.println("<3");
    }


    @Test
    void testAssertThrows() {
        assertThrows(ArithmeticException.class, (() -> mathUtils.divide(5, 0)));
    }


    @Test
    void testAssertAll() {
        List<Integer> result = mathUtils.sortListASC(Arrays.asList(5, 10, 1));
        assertAll("Should return 1, 5, 10",
                () -> assertEquals(1, result.get(0)),
                () -> assertEquals(5, result.get(1)),
                () -> assertEquals(10, result.get(2)));
    }


    @Test
    void testAssertAllV2() {
        List<Integer> result = Arrays.asList(null, 21);
        List<Integer> iterableList = Arrays.asList(1, 2, 3);
        Set<Integer> iterableSet = new HashSet<>(Arrays.asList(1, 2, 3));
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        List<String> stringList1 = Arrays.asList("Java", "\\d{2}", "JUnit");
        List<String> stringList2 = Arrays.asList("Java", "11", "JUnit");
        assertAll(
                () -> assertTrue(5 > 2),
                () -> assertFalse(10 > 20),
                () -> assertEquals(7, 7),
                () -> assertNotEquals(7, 8),
                () -> assertNull(result.get(0)),
                () -> assertNotNull(result.get(1)),
                () -> assertDoesNotThrow(() -> mathUtils.add(2, 2)),
                () -> assertArrayEquals(array1, array2),
                () -> assertIterableEquals(iterableList, iterableSet),
                //() -> assertSame("Java", new String("Java")),//fails
                () -> assertNotSame("Java", new String("Java")),
                () -> assertLinesMatch(stringList1, stringList2));
    }


    @Test
    void testAssertTimeout() {
        //Uruchamia przekazany fragment kodu w tym samym wątku, który wykonuje metodę testową i czeka na jego zakończenie.
        //Po zakończeniu sprawdza, czy założony czas został przekroczony.
        assertTimeout(Duration.ofMillis(50), () -> mathUtils.taskWith20msSleep());
    }


    @Test
    void testAssertTimeoutPreemptively() {
        //Uruchamia przekazany fragment kodu w innym wątku i kończy go natychmiast po przekroczeniu założonego czasu.
        //Kod Executable uruchamiany jest w innym wątku niż ten wywołujący metodę.
        assertTimeoutPreemptively(Duration.ofMillis(50), () -> mathUtils.taskWith20msSleep());
    }

    @Test
    @Disabled
    void testDisabledMethod() {

    }


    @ParameterizedTest(name = "{index} -> number = {arguments} is odd")
    @ValueSource(ints = {1, 3, 5,-3, 15})
    void Should_ReturnTrue_When_isOddIsWorkingProperly(int number) {
        assertTrue(mathUtils.isOdd(number));
    }


    @ParameterizedTest
    @NullSource
    void testNullSource(String nullString) {
        assertThrows(NullPointerException.class, () -> nullString.length());
    }

    @ParameterizedTest
    @EmptySource
    void testEmptySource(List<Integer> integers) {
        assertThrows(IndexOutOfBoundsException.class, () -> integers.get(0));
    }

    @ParameterizedTest
    @EnumSource(SeasonEnum.class)
    void testEnumSource(SeasonEnum seasonEnum) {
        assertTrue(seasonEnum.getNumber() < 5);
    }

    @ParameterizedTest
    @EnumSource(value = SeasonEnum.class, names = {"SPRING", "AUTUMN", "WINTER"}, mode = EnumSource.Mode.EXCLUDE)
    void testSingleEnumSource(SeasonEnum seasonEnum) {
        assertEquals("summer", seasonEnum.name().toLowerCase(Locale.ROOT));
    }


    @ParameterizedTest
    @CsvSource(value = {"2;4", "5;25", "10;100"}, delimiter = ';')
    void testCsvSource(Integer input, Integer expected) {
        assertEquals(expected, mathUtils.getPower(input));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/cars.csv"}, numLinesToSkip = 1)
    void testCsvFileSource(String car, Integer year) {
        assertAll(
                () -> assertTrue(car.equals("Tesla") || car.equals("Charger")),
                () -> assertTrue(year.equals(2021) || year.equals(1969))
        );
    }


    @ParameterizedTest
    @MethodSource("provideAnimalsForTestMethodSource")
    void testMethodSource(Animal inputAnimal) {
        assertTrue(inputAnimal.getName().equals("Killer whale") ||
                inputAnimal.getName().equals("Trout"));
    }
    private static Stream<Arguments> provideAnimalsForTestMethodSource() {
        return Stream.of(
                Arguments.of(new Animal("Killer whale")),
                Arguments.of(new Animal("Trout"))
        );
    }




    @Test
    void assertJ() {
        Animal dog1 = new Animal("Dog");
        Animal dog2 = new Animal("Dog");
        assertThat(dog1).isEqualTo(dog2);

        assertThat("".isEmpty()).isTrue();

        //assertion chaining
        List<String> list = Arrays.asList("a", "b", "c", "d");
        assertThat(list).isNotEmpty().contains("b").startsWith("a").endsWith("d")
                .doesNotContain("e").doesNotContainNull().containsSequence("b", "c");

        assertThat('b').isNotEqualTo('c').isGreaterThan('a').isLowerCase();

        assertThat(Runnable.class).isInterface();
        //upewnij się, że FileNotFoundException dziedziczy po IOException
        assertThat(IOException.class).isAssignableFrom(FileNotFoundException.class);

        File file = new File("src/test/resources/cars.csv");
        assertThat(file).exists().isFile().canRead().canWrite();

        assertThat(5.238).isEqualTo(5.24, withPrecision(0.01));

        Map<Integer, String> map = new HashMap<>();
        map.put(1, "a");
        map.put(2, "b");
        assertThat(map).isNotEmpty().containsKey(2).doesNotContainKeys(10,11).contains(entry(2, "b"));


        Animal animal = new Animal("Lion");
        assertThat(animal.getName()).as("%s name should be equal to Lion", animal.getName())
                .isEqualTo("Lion");
    }

    @Test
    void hamcrest() {
        Animal animal1 = new Animal("Lion");
        Animal animal2 = new Animal("Lion");
        assertThat(animal1, hasProperty("name", equalTo("Lion")));
        assertThat(animal1, samePropertyValuesAs(animal2));
        assertThat(Animal.class, typeCompatibleWith(Object.class));

        List<String> list = Arrays.asList("a", "b", "c", "d");
        assertThat(list, not(empty()));
        assertThat(list, hasSize(4));
        assertThat(list, hasItems("d", "a"));

        Integer number = 5;
        assertThat(number, greaterThan(3));
        assertThat(number, lessThan(10));
        assertThat(5.0, closeTo(4,6));

        String string = "Kobe";
        assertThat(string, equalToIgnoringCase("kobe"));

        //is / not to konstrukcja zwiększająca czytelność i pozwalająca negować asercje
        assertThat("ok", is(equalToIgnoringCase("OK")));
        assertThat("ok", not(equalToIgnoringCase("OKk")));
    }
}
