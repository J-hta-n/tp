package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.displayable.seller.Seller;



/**
 * Deletes a seller identified using it's displayed index from the address book.
 */
public class DeleteSellerCommand extends Command {

    public static final String COMMAND_WORD = "delete-s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the seller identified by the index number used in the displayed seller list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_SELLER_SUCCESS = "Got it. I’ve deleted a seller contact:\n%1$s";

    private final Index targetIndex;

    public DeleteSellerCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Seller> lastShownList = model.getFilteredSellerList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SELLER_DISPLAYED_INDEX);
        }

        Seller sellerToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteSeller(sellerToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_SELLER_SUCCESS, Messages.format(sellerToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteSellerCommand)) {
            return false;
        }

        DeleteSellerCommand otherDeleteSellerCommand = (DeleteSellerCommand) other;
        return targetIndex.equals(otherDeleteSellerCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}