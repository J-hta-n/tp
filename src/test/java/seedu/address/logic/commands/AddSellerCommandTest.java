package seedu.address.logic.commands;


import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalSellers.SALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.displayable.buyer.Buyer;
import seedu.address.model.displayable.seller.Seller;
import seedu.address.testutil.SellerBuilder;
public class AddSellerCommandTest {

    @Test
    public void constructor_nullSeller_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddSellerCommand(null));
    }

    @Test
    public void execute_sellerAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingSellerAdded modelStub = new ModelStubAcceptingSellerAdded();
        Seller validSeller = new SellerBuilder().build();

        CommandResult commandResult = new AddSellerCommand(validSeller).execute(modelStub);

        assertEquals(String.format(AddSellerCommand.MESSAGE_SUCCESS, Messages.format(validSeller)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validSeller), modelStub.sellersAdded);
    }

    @Test
    public void execute_duplicateSeller_throwsCommandException() {
        Seller validSeller = new SellerBuilder().build();
        AddSellerCommand addSellerCommand = new AddSellerCommand(validSeller);
        ModelStub modelStub = new ModelStubWithSeller(validSeller);

        assertThrows(CommandException.class,
                AddSellerCommand.MESSAGE_DUPLICATE_SELLER, () -> addSellerCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Seller alice = new SellerBuilder().withName("Alice").build();
        Seller bob = new SellerBuilder().withName("Bob").build();
        AddSellerCommand addAliceCommand = new AddSellerCommand(alice);
        AddSellerCommand addBobCommand = new AddSellerCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddSellerCommand addAliceCommandCopy = new AddSellerCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different seller -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddSellerCommand addSellerCommand = new AddSellerCommand(SALICE);
        String expected = AddSellerCommand.class.getCanonicalName() + "{toAdd=" + SALICE + "}";
        assertEquals(expected, addSellerCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addBuyer(Buyer buyer) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addSeller(Seller seller) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasBuyer(Buyer buyer) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasSeller(Seller seller) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteBuyer(Buyer target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteSeller(Seller target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setBuyer(Buyer target, Buyer editedBuyer) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSeller(Seller target, Seller editedBuyer) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public ObservableList<Buyer> getFilteredBuyerList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Seller> getFilteredSellerList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredBuyerList(Predicate<? super Buyer> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredSellerList(Predicate<? super Seller> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single seller.
     */
    private class ModelStubWithSeller extends ModelStub {
        private final Seller seller;

        ModelStubWithSeller(Seller seller) {
            requireNonNull(seller);
            this.seller = seller;
        }

        @Override
        public boolean hasSeller(Seller seller) {
            requireNonNull(seller);
            return this.seller.isSamePerson(seller);
        }
    }

    /**
     * A Model stub that always accept the seller being added.
     */
    private class ModelStubAcceptingSellerAdded extends ModelStub {
        final ArrayList<Seller> sellersAdded = new ArrayList<>();

        @Override
        public boolean hasSeller(Seller seller) {
            requireNonNull(seller);
            return sellersAdded.stream().anyMatch(seller::isSamePerson);
        }

        @Override
        public void addSeller(Seller seller) {
            requireNonNull(seller);
            sellersAdded.add(seller);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
