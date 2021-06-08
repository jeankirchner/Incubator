import { options as yargsOptions } from "yargs";

export async function main() {
  //const logger = () => log.with({ origin: "tools-debit" });

  const { argv } = yargsOptions({
    dryRun: {
      alias: "exercise-id",
      type: "string",
      default: true,
    },
  });

  const { exerciseId } = argv;

  console.log(exerciseId);
}
