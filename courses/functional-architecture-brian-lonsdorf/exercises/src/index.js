import { promises as fs } from "fs";
import Stripe from "stripe";
import serverlessMysql from "serverless-mysql";
import { options as yargsOptions } from "yargs";

import * as idempotent from "@flexapp/idempotent";
import { Result } from "@flexapp/result";
import {
  Dispatcher,
  EntityID,
  FlexEvent,
  PublishedEvent,
  Publisher,
  Version,
} from "@flexapp/events-core";
import { log, configure } from "@flexapp/logger";
import { DynamoEventStore } from "@flexapp/events-store-dynamo";
import { StripeCharge } from "@flexapp/user-payment-commands";
import { MySQLStore } from "@flexapp/mysql-store";
import { UserPayment } from "@flexapp/user-payment-types";

configure({ level: "debug" });

const read = async (file: string): Promise<UserPayment> => {
  const buffer = await fs.readFile(file);
  return JSON.parse(buffer.toString("utf8"));
};

const publisher =
  (publish: Publisher): Dispatcher.Publisher =>
  async (
    entity: EntityID,
    event: FlexEvent
  ): Promise<Result<PublishedEvent>> => {
    const pevent = await publish(entity, event, {});

    if (pevent === "version-conflict") {
      return Result.failure("Version conflict");
    }

    return Result.success(pevent);
  };

export async function main() {
  const logger = () => log.with({ origin: "tools-debit" });

  const { argv } = yargsOptions({
    dryRun: {
      alias: "dry-run",
      type: "boolean",
      default: true,
    },
    apiKey: {
      alias: "api-key",
      string: true,
      demandOption: true,
    },
    eventsTable: {
      alias: "events-table",
      string: true,
      demandOption: true,
    },
    dbHost: {
      alias: "database-host",
      string: true,
      demandOption: true,
    },
    dbName: {
      alias: "database-name",
      string: true,
      demandOption: true,
    },
    dbUsername: {
      alias: "database-username",
      string: true,
      demandOption: true,
    },
    dbPassword: {
      alias: "db-password",
      string: true,
      demandOption: true,
    },
    scheduledCharge: {
      alias: "scheduled-charge",
      string: true,
      demandOption: true,
    },
    customer: {
      alias: "customer",
      string: true,
      demandOption: true,
    },
    state: {
      alias: "state",
      string: true,
      demandOption: true,
    },
  });

  const {
    dryRun,
    apiKey,
    eventsTable,
    dbHost,
    dbName,
    dbUsername,
    dbPassword,
    customer,
    scheduledCharge,
    state: _state,
  } = argv;

  const { publish } = DynamoEventStore.create(eventsTable);
  const mysql = serverlessMysql({
    config: {
      host: dbHost,
      database: dbName,
      user: dbUsername,
      password: dbPassword,
    },
  });
  const stripe = new Stripe(apiKey, { apiVersion: "2020-08-27" });

  const config = {
    logger,
    stripe,
    store: () => MySQLStore.create({ logger, mysql: () => mysql }),
    idempotentStore: () => idempotent.DynamoStore.create(eventsTable),
  };

  const command: StripeCharge = {
    type: StripeCharge.command,
    target: { id: customer, type: "customer" },
    payload: {
      scheduledChargeId: `prisma:${scheduledCharge}`,
    },
  };

  const print = async (
    state: UserPayment,
    version: Version,
    _command: StripeCharge,
    _publisher: Dispatcher.Publisher,
    _options: Dispatcher.ExecuteOptions = {}
  ) => {
    console.log({
      state,
      version,
      message: "DRY RUN",
    });
    return Result.success(true);
  };

  const state = await read(_state);

  const process = dryRun ? print : StripeCharge.handler(config);

  const result = await process(
    state,
    "unknown",
    command,
    publisher(publish),
    {}
  );

  result.on(
    () => console.log({ command, message: "successfully processed command" }),
    (e) => {
      console.log({ e });
    }
  );
}
