-- Enable UUID generator (if not already enabled)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

--------------------------------------------------------------------
-- CLIENTS
--------------------------------------------------------------------
INSERT INTO clients (id, name, company, email, phone, industry, notes)
VALUES
    (DEFAULT, 'Sarah Thompson', 'Thompson Retail Co.', 'sarah@thompsonretail.com', '+1-415-555-2100', 'Retail',
     'High-value lead interested in automation. Responds well to data-driven proposals.'),

    (DEFAULT, 'Michael Brooks', 'Brooks Financial Group', 'michael@brooksgroup.com', '+1-646-555-1188', 'Finance',
     'Busy executive. Prefers concise communication. Opportunity for long-term retainer.'),

    (DEFAULT, 'Emily Carter', 'Carter Creative Studio', 'emily@cartercreative.co', '+1-310-555-0990', 'Marketing',
     'Looking for recurring support + integrations. Very responsive on email.')
;

--------------------------------------------------------------------
-- DEALS
--------------------------------------------------------------------
INSERT INTO deals (id, client_id, title, stage, value, probability, expected_close_date)
VALUES
    (DEFAULT,
     (SELECT id FROM clients WHERE name='Sarah Thompson'),
     'Retail Workflow Automation Pilot',
     'DISCOVERY',
     8500.00,
     40,
     '2025-12-15'),

    (DEFAULT,
     (SELECT id FROM clients WHERE name='Sarah Thompson'),
     'E-Commerce Analytics Dashboard',
     'PROPOSAL',
     14500.00,
     65,
     '2026-01-10'),

    (DEFAULT,
     (SELECT id FROM clients WHERE name='Michael Brooks'),
     'Financial Advisor CRM Revamp',
     'LEAD',
     12000.00,
     25,
     '2026-01-20'),

    (DEFAULT,
     (SELECT id FROM clients WHERE name='Emily Carter'),
     'Marketing Automation Micro-SaaS',
     'PROPOSAL',
     18000.00,
     70,
     '2025-12-20'),

    (DEFAULT,
     (SELECT id FROM clients WHERE name='Emily Carter'),
     'Client Portal + File Delivery System',
     'DISCOVERY',
     9500.00,
     45,
     '2026-02-01')
;

--------------------------------------------------------------------
-- ACTIVITIES (3–4 each)
--------------------------------------------------------------------
-- For Sarah #1
INSERT INTO activities (id, deal_id, type, title, description, completed)
VALUES
    (DEFAULT, (SELECT id FROM deals WHERE title='Retail Workflow Automation Pilot'),
     'CALL', 'Initial qualification call',
     'Discussed current bottlenecks in inventory and POS syncing.', true),

    (DEFAULT, (SELECT id FROM deals WHERE title='Retail Workflow Automation Pilot'),
     'EMAIL', 'Follow-up material sent',
     'Sent sample automation flows + timeline expectations.', true),

    (DEFAULT, (SELECT id FROM deals WHERE title='Retail Workflow Automation Pilot'),
     'MEETING', 'Requirements deep-dive',
     'Scheduled for next week to map out integrations.', false);

-- Sarah #2
INSERT INTO activities (id, deal_id, type, title, description, completed)
VALUES
    (DEFAULT, (SELECT id FROM deals WHERE title='E-Commerce Analytics Dashboard'),
     'NOTE', 'Opportunity: high confidence',
     'Strong need for unified dashboards + automated reporting.', true),

    (DEFAULT, (SELECT id FROM deals WHERE title='E-Commerce Analytics Dashboard'),
     'MEETING', 'Demo of sample dashboards',
     'Walkthrough scheduled for Thursday.', false),

    (DEFAULT, (SELECT id FROM deals WHERE title='E-Commerce Analytics Dashboard'),
     'EMAIL', 'Sent proposal draft',
     'Shared draft pricing + feature breakdown.', false);

-- Michael Brooks
INSERT INTO activities (id, deal_id, type, title, description, completed)
VALUES
    (DEFAULT, (SELECT id FROM deals WHERE title='Financial Advisor CRM Revamp'),
     'CALL', 'Initial contact attempt',
     'Left voicemail. Will retry tomorrow morning.', false),

    (DEFAULT, (SELECT id FROM deals WHERE title='Financial Advisor CRM Revamp'),
     'EMAIL', 'Intro email sent',
     'Sent brief overview of how the CRM overhaul could look.', true),

    (DEFAULT, (SELECT id FROM deals WHERE title='Financial Advisor CRM Revamp'),
     'NOTE', 'High potential retainer',
     'Could turn into multi-month engagement if closed.', false);

-- Emily #1
INSERT INTO activities (id, deal_id, type, title, description, completed)
VALUES
    (DEFAULT, (SELECT id FROM deals WHERE title='Marketing Automation Micro-SaaS'),
     'MEETING', 'Feature workshop',
     'Mapped user flows for automation triggers + metrics.', true),

    (DEFAULT, (SELECT id FROM deals WHERE title='Marketing Automation Micro-SaaS'),
     'EMAIL', 'Sent proposal V1',
     'Includes pricing tiers + hosting strategy.', true),

    (DEFAULT, (SELECT id FROM deals WHERE title='Marketing Automation Micro-SaaS'),
     'CALL', 'Clarification call',
     'Client wants additional Zapier-style integrations.', false);

-- Emily #2
INSERT INTO activities (id, deal_id, type, title, description, completed)
VALUES
    (DEFAULT, (SELECT id FROM deals WHERE title='Client Portal + File Delivery System'),
     'CALL', 'Kickoff discussion',
     'Clarified login flows + permissioning.', true),

    (DEFAULT, (SELECT id FROM deals WHERE title='Client Portal + File Delivery System'),
     'NOTE', 'Client prefers quick turnaround',
     'Targeting a January MVP.', true),

    (DEFAULT, (SELECT id FROM deals WHERE title='Client Portal + File Delivery System'),
     'EMAIL', 'Next-steps email',
     'Asking for design assets + brand style guide.', false);

--------------------------------------------------------------------
-- PROPOSALS (0–2 in realistic patterns)
--------------------------------------------------------------------
-- Sarah #2 (analytics dashboard)
INSERT INTO proposals (id, deal_id, title, description, status, pricing_items)
VALUES
    (DEFAULT,
     (SELECT id FROM deals WHERE title='E-Commerce Analytics Dashboard'),
     'Analytics Dashboard — Full Proposal',
     'Comprehensive dashboard system with automated reporting + role-based views.',
     'SENT',
     '[
        {"description": "Core Dashboard Build", "quantity": 1, "price": 8500},
        {"description": "Automated Email Reports", "quantity": 1, "price": 3000},
        {"description": "Deployment + Hosting Setup", "quantity": 1, "price": 1200}
     ]'::jsonb
    ),

    (DEFAULT,
     (SELECT id FROM deals WHERE title='E-Commerce Analytics Dashboard'),
     'Analytics Dashboard — Lite Option',
     'Reduced scope version focused on essentials and shorter timeline.',
     'PREPARED',
     '[
        {"description": "Dashboard Essentials", "quantity": 1, "price": 6000},
        {"description": "Basic Reporting", "quantity": 1, "price": 1500}
     ]'::jsonb
    );

-- Emily #1 (automation micro-SaaS)
INSERT INTO proposals (id, deal_id, title, description, status, pricing_items)
VALUES
    (DEFAULT,
     (SELECT id FROM deals WHERE title='Marketing Automation Micro-SaaS'),
     'Micro-SaaS Proposal',
     'Automation engine MVP with campaign triggers and analytics.',
     'SENT',
     '[
        {"description": "MVP Backend", "quantity": 1, "price": 9000},
        {"description": "Automation Rules Engine", "quantity": 1, "price": 4500},
        {"description": "Dashboard UI", "quantity": 1, "price": 3500}
     ]'::jsonb
    );
