package net.minestom.server.command.builder.arguments;

import net.minestom.server.command.ArgumentParserType;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.network.NetworkBuffer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A word argument which is defined at each command execution
 */
public class ArgumentDynamic<T> extends Argument<T> {
    private final Supplier<Collection<String>> suggestionCallback;
    private final Function<String, T> parser;

    public ArgumentDynamic(String id, Supplier<Collection<String>> suggestionCallback, Function<String, T> parser) {
        super(id);
        this.parser = parser;
        this.suggestionCallback = suggestionCallback;
        setSuggestionCallback((_, _, suggestion) -> {
            for (String suggestionString : suggestionCallback.get()) {
                suggestion.addEntry(new SuggestionEntry(suggestionString));
            }
        });
    }

    @Override
    public T parse(CommandSender sender, String input) throws ArgumentSyntaxException {
        if (input.contains(" ")) throw new ArgumentSyntaxException("Word cannot contain space character", input, ArgumentWord.SPACE_ERROR);
        if (suggestionCallback.get().contains(input))
            return parser.apply(input);
        throw new ArgumentSyntaxException("Invalid word", input, ArgumentWord.RESTRICTION_ERROR);
    }



    @Override
    public ArgumentParserType parser() {
        return ArgumentParserType.STRING;
    }

    @Override
    public byte @Nullable [] nodeProperties() {
        return NetworkBuffer.makeArray(NetworkBuffer.VAR_INT, 0); // Single word
    }

    @Override
    public String toString() {
        return String.format("DynamicWord<%s>", getId());
    }
}
