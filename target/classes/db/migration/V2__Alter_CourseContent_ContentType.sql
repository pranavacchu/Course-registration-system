-- First, create a temporary column
ALTER TABLE course_content ADD COLUMN content_type_new varchar(255);

-- Update the new column with string values based on the old numeric values
UPDATE course_content SET content_type_new = 
    CASE content_type
        WHEN 0 THEN 'LECTURE'
        WHEN 1 THEN 'ASSIGNMENT'
        WHEN 2 THEN 'QUIZ'
        WHEN 3 THEN 'READING'
        WHEN 4 THEN 'OTHER'
        ELSE 'OTHER'
    END;

-- Drop the old column
ALTER TABLE course_content DROP COLUMN content_type;

-- Rename the new column to the original name
ALTER TABLE course_content RENAME COLUMN content_type_new TO content_type; 