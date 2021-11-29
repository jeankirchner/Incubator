import { options as yargsOptions } from "yargs";

import './ex1';

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
