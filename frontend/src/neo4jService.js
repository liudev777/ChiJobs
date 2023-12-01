import neo4j from 'neo4j-driver';

const driver = neo4j.driver(
    'bolt://localhost:7687',
    neo4j.auth.basic('your_username', 'your_password')
);

const neo4jService = {
    insertApplications: async (applications) => {
        const session = driver.session();

        try {
        for (const application of applications) {
            await session.run(`
                MERGE (p:Person {email: $email})
                ON CREATE SET p.firstName = $firstName, p.lastName = $lastName
                MERGE (j:Job {jobId: $jobId})
                MERGE (p)-[:APPLIED_FOR {applicationId: $applicationId}]->(j)
            `, application);
        }
        } finally {
        session.close();
        }
    },

    getOtherApplicants: async (jobId, email) => {
        const session = driver.session();

        try {
            const result = await session.run(`
                MATCH (j:Job {jobId: $jobId})<-[:APPLIED_FOR]-(p:Person)
                WHERE p.email <> $email
                RETURN p;
            `, { jobId, email });

            return result.records.map(record => record.get('p').properties);
        } finally {
            session.close();
        }
    },


    close: () => {
        driver.close();
    },
};

export default neo4jService;
