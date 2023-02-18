INSERT INTO db_natura.user (username, email, password, flower_validator, insect_validator)
	VALUES ('marion', 'marion.delmas2@hotmail.fr','pwd', true, true)
    ON DUPLICATE KEY UPDATE password = 'pwd';
INSERT INTO db_natura.entry (name, discriminator, user_id) 
	SELECT * FROM (SELECT 'butterfly', 'Insect', (SELECT id FROM db_natura.user WHERE username='marion')) AS tmp
    WHERE NOT EXISTS (SELECT name FROM db_natura.entry WHERE name = 'butterfly') LIMIT 1;
INSERT INTO db_natura.criteria (name, value) 
	SELECT * FROM (SELECT 'size', 'between 25 and 55 mm') AS temp
    WHERE NOT EXISTS (SELECT 1 FROM db_natura.criteria WHERE name = 'size' AND value = 'between 25 and 55 mm');
INSERT INTO db_natura.species (common_name, scientific_name, type, family, classification_order) 
	VALUES ('Citron', 'Gonepteryx rhamni', 'Insect', 'Pieridae', 'Lepidoptera')
    ON DUPLICATE KEY UPDATE type = 'Insect', family = 'Pieridae', classification_order ='Lepidoptera';
INSERT IGNORE  INTO db_natura.species_criteria (species_id, criteria_id) 
	VALUES ((SELECT id FROM db_natura.species WHERE common_name='Citron'), (SELECT id FROM db_natura.criteria WHERE name='size' AND value='between 25 and 55 mm'));
INSERT IGNORE  INTO db_natura.identification (entry_id, species_id, user_id, suggested_date) 
	VALUES ((SELECT id FROM db_natura.entry WHERE name='butterfly'), (SELECT id FROM db_natura.species WHERE common_name='Citron'), (SELECT id FROM db_natura.user WHERE username='marion'), (SELECT CURRENT_TIMESTAMP()));