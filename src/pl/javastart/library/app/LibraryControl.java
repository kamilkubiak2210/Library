package pl.javastart.library.app;

import pl.javastart.library.Exceptions.*;
import pl.javastart.library.io.ConsolePrinter;
import pl.javastart.library.io.DataReader;
import pl.javastart.library.io.file.FileManager;
import pl.javastart.library.io.file.FileManagerBuilder;
import pl.javastart.library.model.*;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Optional;


public class LibraryControl {


    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader(printer);
    private FileManager fileManager;

    public LibraryControl() {
        fileManager = new FileManagerBuilder(printer, dataReader).build();
        try {
            library = fileManager.importData();
            printer.printLine("Zaimportowano dane z pliku ");
        } catch (DataImportException | InvaildDataException e) {
            printer.printLine(e.getMessage());
            printer.printLine("Zainicjonowano nową bazę.");
            library = new Library();
        }
    }

    private Library library;


    public void controlLoop() {
        Option option;
        do {
            printOptions();
            option = getOption();
            switch (option) {
                case ADD_BOOK:
                    addBook();
                    break;
                case ADD_MAGAZINE:
                    addMagazine();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                    break;
                case PRINT_MAGAZINES:
                    printMagazines();
                    break;
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
                    break;
                case ADD_USER:
                    addUser();
                    break;
                case PRINT_USERS:
                    printUsers();
                    break;
                case FIND_BOOK:
                    findBook();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    printer.printLine("Nie ma takiej opcji, wprowadź ponownie.");
            }
        } while (option != Option.EXIT);
    }

    private void findBook() {
        printer.printLine("Podaj tytuł publikacji:");
        String title=dataReader.getString();
        String notFoundMessage="Brak publiakcji o takim tytule";
        library.findPublicationByTitle(title).map(Publication::toString).ifPresentOrElse(System.out::println,()-> System.out.println(notFoundMessage));
    }

    private void printBooks() {
        printer.printBooks(library.getSortedPublications(
                Comparator.comparing(Publication::getTitle,String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void printMagazines() {
        printer.printMagazines(library.getSortedPublications(
                Comparator.comparing(Publication::getTitle,String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void printUsers() {
        printer.printUsersCollection(library.getSortedUsers(
                Comparator.comparing(User::getLastName,String.CASE_INSENSITIVE_ORDER)
        ));
    }


    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser();
        try {
            library.addUser(libraryUser);
        } catch (UserArleadyExistsException e) {
            printer.printLine(e.getMessage());
        }
    }


    private Option getOption() {
        boolean optionOk = false;
        Option option = null;
        while (!optionOk) {
            try {
                option = Option.createFromInt(dataReader.getInt());
                optionOk = true;
            } catch (NoSuchOptionException e) {
                printer.printLine(e.getMessage());
            } catch (InputMismatchException e) {
                printer.printLine("Wprowadzono wartośc, która nie jest liczbą, podaj ponownie:");
            }
        }
        return option;
    }


    private void addMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addPublication(magazine);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć magazynu, niepoprawne dane");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Osiągnieto limit pojemności, nie można dodać kolejnego magazynu");
        }
    }

    private void deleteMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            if (library.removePublication(magazine)) {
                printer.printLine("Usunięto magazyn");
            } else {
                printer.printLine("Brak wskazanego magazynu");
            }
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć magazynu, niepoprawne dane");
        }

    }

    private void exit() {
        try {
            fileManager.exportData(library);
            printer.printLine("Export danych do pliku został zakończony powodzeniem");
        } catch (DataExportException e) {
            printer.printLine(e.getMessage());
        }
        printer.printLine("Koniec programu, papa!");
        dataReader.close();
    }


    private void addBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            library.addPublication(book);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć książki, niepoprawne dane");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Osiągnieto limit pojemności, nie można dodać kolejnej książki");
        }
    }

    private void deleteBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            if (library.removePublication(book)) {
                printer.printLine("Usunięto książkę");
            } else {
                printer.printLine("Brak wskazanej książki");
            }
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć ksiązki, niepoprawne dane");
        }
    }

    private void printOptions() {
        printer.printLine("Wybierz opcję:");
        for (Option option : Option.values()) {
            printer.printLine(option.toString());

        }
    }

    private enum Option {
        EXIT(0, " - Wyjście z programu"),
        ADD_BOOK(1, " - dodanie nowej książki"),
        ADD_MAGAZINE(2, " - dodanie nowego magazynu"),
        PRINT_BOOKS(3, " - wyświetl dostępne książki"),
        PRINT_MAGAZINES(4, " - wyświetl dostepne magazyny"),
        DELETE_BOOK(5, " - usuń ksiązkę"),
        DELETE_MAGAZINE(6, " - usuń magazyn"),
        ADD_USER(7, " - dodaj czytelnika"),
        PRINT_USERS(8, " - Wyświetl czytelników"),
        FIND_BOOK(9," - Wyszukaj książkę");


        private final int value;
        private final String description;

        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return value + description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException {
            try {
                return Option.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("Brak opcji o id " + option);
            }
        }
    }
}


