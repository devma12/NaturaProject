# reset all tables
SET FOREIGN_KEY_CHECKS = 0; 
TRUNCATE TABLE db_natura.identification;
TRUNCATE TABLE db_natura.species_criteria;
TRUNCATE TABLE db_natura.species;
TRUNCATE TABLE db_natura.criteria;
TRUNCATE TABLE db_natura.entry;
TRUNCATE TABLE db_natura.user;
SET FOREIGN_KEY_CHECKS = 1; 

# create one new user
INSERT INTO db_natura.user (username, email, password)
	VALUES ('marion', 'marion.delmas2@hotmail.fr','pwd');

# create one entry done by the previously created user
INSERT INTO db_natura.entry (name, user_id)
	VALUES ('butterfly', (SELECT id FROM db_natura.user WHERE username='marion'));

# create one generic criteria
INSERT INTO db_natura.criteria (name, value)
	VALUES ('size', 'between 25 and 55 mm');
    
# create one species
INSERT INTO db_natura.species (common_name, scientific_name, type)
	VALUES ('Citron', 'Gonepteryx rhamni', 'Insect');

#  specify one criteria to one species
INSERT INTO db_natura.species_criteria (species_id, criteria_id)
	VALUES ((SELECT id FROM db_natura.species WHERE common_name='Citron'), 
			(SELECT id FROM db_natura.criteria WHERE name='size' AND value='between 25 and 55 mm'));
    
# add one identification
INSERT INTO db_natura.identification (entry_id, species_id, user_id, suggested_date)
	VALUES ((SELECT id FROM db_natura.entry WHERE name='butterfly'),
			(SELECT id FROM db_natura.species WHERE common_name='Citron'),
            (SELECT id FROM db_natura.user WHERE username='marion'),
            (SELECT CURRENT_TIMESTAMP()));
