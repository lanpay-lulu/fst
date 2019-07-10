package example;

import fst.*;
import utils.BytesRef;
import utils.BytesRefBuilder;
import utils.IntsRef;
import utils.IntsRefBuilder;

import java.io.IOException;

public class TestLuceneFST {


    public static void main(String[] args) throws IOException {
        String inputValues[] = {"cat", "dog", "dogs", "dogs3721kkkkkkkkkkkkk"}; // in order
        long outputValues[] = {5, 7, 12, 28}; // must be positive
        PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
        Builder<Long> builder = new Builder<Long>(FST.INPUT_TYPE.BYTE1, outputs);
        BytesRefBuilder bytesRefBuilder = new BytesRefBuilder();
        IntsRefBuilder scratchInts = new IntsRefBuilder();
        for (int i = 0; i < inputValues.length; i++) {
            bytesRefBuilder.copyChars(inputValues[i]);
            builder.add(Util.toIntsRef(bytesRefBuilder.get(), scratchInts), outputValues[i]);
        }
        FST<Long> fst = builder.finish();

        // retrieval by key
        Long value = Util.get(fst, new BytesRef("dog"));
        System.out.println(value);

        // retrieval by value
        IntsRef ref = Util.getByOutput(fst, 12);
        String input = Util.toBytesRef(ref, bytesRefBuilder).utf8ToString();
        System.out.println(input);

        // scanning
        IntsRefFSTEnum<Long> fstEnum = new IntsRefFSTEnum<>(fst);
        IntsRefFSTEnum.InputOutput<Long> inputOutput;
        while ((inputOutput = fstEnum.next()) != null) {
            input = Util.toBytesRef(inputOutput.input, bytesRefBuilder).utf8ToString();
            Long output = inputOutput.output;
            System.out.println(input + "\t" + output);
        }

        // Deduction

    }

    public static void main2(String[] args) throws IOException {
        String[] inputValues = {"cat", "dog", "dogs", "dogs3721kkkkkkkkkkkkk"}; // in order
        String[] outputValues = {"ab", "abc", "abd", "bcddppppppp"}; // must be positive
        ByteSequenceOutputs outputs = ByteSequenceOutputs.getSingleton();

        Builder<BytesRef> builder = new Builder<>(FST.INPUT_TYPE.BYTE1, outputs);
        BytesRefBuilder bytesRefBuilder = new BytesRefBuilder();
        IntsRefBuilder scratchInts = new IntsRefBuilder();
        for (int i = 0; i < inputValues.length; i++) {
            bytesRefBuilder.copyChars(inputValues[i]);
            //builder.add(Util.toIntsRef(bytesRefBuilder.get(), scratchInts), outputValues[i]);
            scratchInts.copyUTF8Bytes(bytesRefBuilder.get());
            builder.add(scratchInts.get(), new BytesRef(outputValues[i]));
        }
        FST<BytesRef> fst = builder.finish();

        // retrieval by key
        BytesRef value = Util.get(fst, new BytesRef("dog"));
        System.out.println(value.utf8ToString());
        System.out.println("fst=");

        // retrieval by value. only support Long output


        // scanning
        IntsRefFSTEnum<BytesRef> fstEnum = new IntsRefFSTEnum<>(fst);
        IntsRefFSTEnum.InputOutput<BytesRef> inputOutput;
        while ((inputOutput = fstEnum.next()) != null) {
            String input = Util.toBytesRef(inputOutput.input, bytesRefBuilder).utf8ToString();
            BytesRef output = inputOutput.output;
            String out = output.utf8ToString();
            System.out.println(input + "\t" + out);
        }

        // Deduction

    }
}
