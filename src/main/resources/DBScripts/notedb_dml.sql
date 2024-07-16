/* insert user data */
INSERT INTO public.t_users (id, email, name, password)
VALUES (1, 'bob@speer.com', 'Bob', '$2a$10$/7XOiRfzv31HKNLbgbM7GOUuGX2zvrfeUOdGZbduIQiAopirVJlja');
INSERT INTO public.t_users (id, email, name, password)
VALUES (2, 'mike@speer.com', 'Mike', '$2a$10$/7XOiRfzv31HKNLbgbM7GOUuGX2zvrfeUOdGZbduIQiAopirVJlja');
INSERT INTO public.t_users (id, email, name, password)
VALUES (3, 'jone@speer.com', 'Jone', '$2a$10$7LBjtBGe9/Val.xOi1EH9ejHIxWcMv0wX3MjpJew8i0h1fTZR1WRy');

/* insert note data */
INSERT INTO public.t_notes (id, title, author_id, content)
VALUES (1, 'aws notes', 1, 'aws books');
INSERT INTO public.t_notes (id, title, author_id, content)
VALUES (2, 'science notes', 1, 'science books');
INSERT INTO public.t_notes (id, title, author_id, content)
VALUES (3, 'magzine notes', 2, 'magzine reports');
INSERT INTO public.t_notes (id, title, author_id, content)
VALUES (4, 'essay notes', 2, 'essay books');
INSERT INTO public.t_notes (id, title, author_id, content)
VALUES (5, 'comic notes', 3, 'comic books');
INSERT INTO public.t_notes (id, title, author_id, content)
VALUES (6, 'Trump safe after apparent assassination attempt at rally that left suspect, 1 attendee dead', 2,
        'Donald Trump appeared to be the target of an assassination attempt as he spoke during a rally in Pennsylvania on Saturday, law enforcement officials said.<br/>The former president, his ear covered in blood from what he said was a gunshot, was quickly pulled away by U.S. Secret Service agents and his campaign said he was safe.<br>A local prosecutor said the suspected gunman — who FBI identified as 20-year-old Thomas Matthew Crooks of Bethel Park, Pa. — and at least one attendee are dead. The Secret Service said two spectators were critically injured.<br/>Posting on his Truth Social media site about two and a half hours after the shooting, Trump said a bullet "pierced the upper part of my right ear."<br/>"I knew immediately that something was wrong in that I heard a whizzing sound, shots and immediately felt the bullet ripping through the skin," he said in the post. "Much bleeding took place, so I realized then what was happening."');
INSERT INTO public.t_notes (id, title, author_id, content)
VALUES (7, 'Midnight work', 3, 'Endless work');

/* insert note_share data */
INSERT INTO public.t_note_share (id, note_id, user_id)
VALUES (1, 5, 1);
INSERT INTO public.t_note_share (id, note_id, user_id)
VALUES (2, 5, 2);